# Self-hosted Remote Server

> server provide connection beetween mobile app and ide

## Docs
* [Русский](docs/README_RU.md) - описание того, как работает сервер, его особенности.
* [English](docs/README_US.md) - a description of how the server works, its features.

## Quick start

### Installing a repository using a standard git clone

```bash
  git clone https://github.com/yourname/remote-pycharm
```
> go to "apps/servers/self-hosted-remote-server"

— Build container:

```docker
  docker build -f deployment/Dockerfile -t your_name .
```

— Run docker container:

```bash
  docker run -d --restart=always --name your_name -p 8765:8765 -e AUTH_TOKEN=your_auth_token
```
