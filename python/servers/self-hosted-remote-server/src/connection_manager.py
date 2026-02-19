from fastapi import WebSocket
from typing import Dict
from models import ClientType
import json

class ConnectionManager:
    def __init__(self):
        self.connections: Dict[ClientType, WebSocket] = {}

    def is_connected(self, client_type: ClientType) -> bool:
        return client_type in self.connections

    async def register(self, client_type: ClientType, websocket: WebSocket):
        self.connections[client_type] = websocket

    async def unregister(self, client_type: ClientType):
        self.connections.pop(client_type, None)

    async def send(self, target: ClientType, message: dict):
        ws = self.connections.get(target)
        if ws:
            await ws.send_text(json.dumps(message))

    async def route(self, sender: ClientType, message: dict):
        target = (
            ClientType.ide if sender == ClientType.android
            else ClientType.android
        )

        if target not in self.connections:
            await self.send(sender, {
                "type": "error",
                "message": f"{target} not connected"
            })
            return

        await self.send(target, message)