import os

AUTH_TOKEN = os.getenv("AUTH_TOKEN")

if not AUTH_TOKEN:
    raise RuntimeError("AUTH_TOKEN is not set")