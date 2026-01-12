from fastapi import WebSocket, WebSocketDisconnect
import json
from models import HandshakeMessage, ClientType
from auth import authenticate
from connection_manager import ConnectionManager

manager = ConnectionManager()


async def websocket_handler(websocket: WebSocket):
    await websocket.accept()
    client_type = None

    try:
        raw = await websocket.receive_text()
        handshake = HandshakeMessage(**json.loads(raw))

        if not authenticate(handshake.auth_token):
            await websocket.close(code=1008, reason="Auth failed")
            return

        if manager.is_connected(handshake.client_type):
            await websocket.close(code=1008, reason="Client already connected")
            return

        client_type = handshake.client_type
        await manager.register(client_type, websocket)

        await websocket.send_text(json.dumps({
            "type": "handshake_ack",
            "status": "connected"
        }))

        while True:
            message = json.loads(await websocket.receive_text())
            await manager.route(client_type, message)

    except WebSocketDisconnect:
        if client_type:
            await manager.unregister(client_type)

    except Exception as e:
        await websocket.close(code=1011, reason=str(e))