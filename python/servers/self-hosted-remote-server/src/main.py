from fastapi import FastAPI, WebSocket, WebSocketDisconnect
from fastapi.responses import JSONResponse
import uvicorn
import json
import os
from typing import Optional

app = FastAPI(docs_url=None, redoc_url=None, openapi_url=None)

android_websocket: Optional[WebSocket] = None
ide_websocket: Optional[WebSocket] = None

AUTH_TOKEN = os.getenv("AUTH_TOKEN")


class ConnectionManager:
    @staticmethod
    async def authenticate(auth_token: str, client_type: str) -> bool:
        if auth_token != AUTH_TOKEN:
            return False

        if client_type == "android" and android_websocket is not None:
            return False

        if client_type == "ide" and ide_websocket is not None:
            return False

        return True

    @staticmethod
    async def register_client(websocket: WebSocket, client_type: str):

        global android_websocket, ide_websocket

        if client_type == "android":
            android_websocket = websocket

        elif client_type == "ide":
            ide_websocket = websocket

            if android_websocket:
                await android_websocket.send_text(json.dumps({"type": "connection",
                                                              "message": "ide connected"}))

    async def unregister_client(self, client_type: str):

        global android_websocket, ide_websocket

        if client_type == "android":

            android_websocket = None

        elif client_type == "ide":
            if android_websocket:
                await self.route_message({"type": "error", "message": "ide disconnected"},
                                         "ide")
                await android_websocket.close()

            ide_websocket = None

    @staticmethod
    async def route_message(message: dict, sender_type: str):

        try:
            if sender_type == "android" and ide_websocket:
                await ide_websocket.send_text(json.dumps(message))

            elif sender_type == "android" and not ide_websocket:
                if android_websocket:
                    await android_websocket.send_text(json.dumps({
                        "type": "error",
                        "message": "IDE not connected"
                    }))

            elif sender_type == "ide" and android_websocket:
                await android_websocket.send_text(json.dumps(message))

        except Exception as e:
            print(f"Routing error: {e}")


manager = ConnectionManager()


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()

    client_type = None

    try:
        handshake_data = await websocket.receive_text()
        handshake = json.loads(handshake_data)

        if handshake.get("type") != "handshake":
            await websocket.close(code=1008, reason="First message must be handshake")
            return

        client_type = handshake.get("client_type")
        auth_token = handshake.get("auth_token")

        if client_type not in ["android", "ide"]:
            await websocket.close(code=1008, reason="Invalid client_type")
            return

        if not await manager.authenticate(auth_token, client_type):
            await websocket.close(code=1008, reason="Authentication failed")
            return

        await manager.register_client(websocket, client_type)

        await websocket.send_text(json.dumps({
            "type": "handshake_ack",
            "status": "connected"
        }))

        while True:
            message_data = await websocket.receive_text()
            message = json.loads(message_data)

            await manager.route_message(message, client_type)

    except WebSocketDisconnect:
        if client_type:
            await manager.unregister_client(client_type)

    except json.JSONDecodeError:
        await websocket.close(code=1007, reason="Invalid JSON")

    except Exception as e:
        await websocket.close(code=1011, reason=f"Internal error: {e}")


@app.get("/")
async def root():
    return JSONResponse({
        "service": "Self-hosted remote server",
        "status": "running"
    })


if __name__ == "__main__":
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=8765,
        ssl_keyfile="key.pem",
        ssl_certfile="cert.pem",
        log_level="info"
    )
