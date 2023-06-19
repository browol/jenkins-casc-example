## How to build using buildkit
```bash
buildctl \
--addr $BUILDKIT_ADDR \
build --frontend dockerfile.v0 \
--local context=nginx/nginx-unprivileged/ \
--local dockerfile=nginx/nginx-unprivileged/Dockerfile
```
