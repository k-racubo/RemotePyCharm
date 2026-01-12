from config import AUTH_TOKEN


def authenticate(token: str) -> bool:
    return token == AUTH_TOKEN