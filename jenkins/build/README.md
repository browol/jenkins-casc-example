## How to build

For base image
```bash
az acr build -t jenkins/jenkins:base-v2.401.1 -r $ACR_NAME.azurecr.io . -f Dockerfile.base
```

For extended image
```bash
az acr build . \
-t jenkins/jenkins:extended-v2.401.1 \
-r $ACR_NAME.azurecr.io \
-f Dockerfile.extended \
--build-arg IMAGE="$ACR_NAME.azurecr.io/jenkins/jenkins" \
--build-arg TAG="base-v2.401.1"
```

**NOTE:** `az acr login -n $ACR_NAME` is required before execute the commands above.
