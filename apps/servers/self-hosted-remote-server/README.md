# Self-hosted Remote Server

> server provide connection beetween mobile app and ide

## Docs
* [Русский](docs/README_RU.md) - описание того, как работает сервер, его особенности.
* [English](docs/README_US.md) - a description of how the server works, its features.

## Quick start

### Installing only server part

```bash
  # 1. Clone repository
  git clone --filter=blob:none --sparse https://github.com/k-racubo/Remote-PyCharm
  cd Remote-PyCharm

  # 2. Switch to develop branch
  git checkout develop

  # 3. Configure sparse-checkout for server
  git sparse-checkout set apps/servers/self-hosted-remote-server

  # 4. Get the files
  git checkout
```

— Build and run with Docker

```bash
  # build container
  cd self-hosted-remote-server
  docker build -f deployment/Dockerfile -t your_name .
  
  # run container
  docker run -d --restart=always --name your_name -p 8765:8765 -e AUTH_TOKEN=your_auth_token
```
> important: you must set AUTH_TOKEN environment variable when starting container

