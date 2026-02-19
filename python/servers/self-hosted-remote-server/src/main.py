from fastapi import FastAPI, WebSocket
from websocket import websocket_handler
from fastapi.responses import JSONResponse
import uvicorn

app = FastAPI(docs_url=None, redoc_url=None, openapi_url=None)


@app.get("/")
async def root():
    return JSONResponse({
        "service": "Self-hosted remote server",
        "status": "running"
    })

@app.websocket("/ws")
async def ws_endpoint(ws: WebSocket):
    await websocket_handler(ws)

if __name__ == "__main__":
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=8765,
        ssl_keyfile="key.pem",
        ssl_certfile="cert.pem",
        log_level="info"
    )
