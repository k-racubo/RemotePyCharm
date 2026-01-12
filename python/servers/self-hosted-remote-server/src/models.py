from enum import Enum
from pydantic import BaseModel


class ClientType(str, Enum):
    android = "android"
    ide = "ide"


class HandshakeMessage(BaseModel):
    type: str
    client_type: ClientType
    auth_token: str