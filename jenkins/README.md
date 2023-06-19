# Jenkins

## How to build

For base image
```bash
az acr build -t jenkins/jenkins:base-v2.401.1 -r acrdemo3fh45a.azurecr.io . -f Dockerfile.base
```

For extended image
```bash
az acr build -t jenkins/jenkins:extended-v2.401.1 -r acrdemo3fh45a.azurecr.io . -f Dockerfile.extended
```

**NOTE:** `az acr login -n acrdemo3fh45a` is required before execute the commands above.

## How to deploy into the Kubernetes cluster

The `az login` and `az aks get-credentials` is required in order to access Kubernetes cluster.

### How to deploy Jenkins
```bash
kubectl kustomize deploy/jenkins/ | kubectl apply -f -
```

### How to deploy Ingress-nginx
```bash
kubectl kustomize --enable-helm deploy/ingress-nginx/ | kubectl apply -f -
```

### How to add git token credentials into Jenkins

Replace `<TOKEN>` with Personal Access Token with repository access permissions from Github.

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: git-token-credentials
  namespace: infra-jenkins
  labels:
    jenkins.io/credentials-type: usernamePassword
  annotations:
    jenkins.io/credentials-description : "Git Credentials from Kubernetes"
type: Opaque
stringData:
  username: browol@github.com
  password: <TOKEN>
```

Then, save as `git-token-credentials.yaml` file and run `kubectl -f git-token-credentials.yaml`

See more information at https://jenkinsci.github.io/kubernetes-credentials-provider-plugin/examples/

### How to add webhook token into Jenkins

Replace `<SECURE_RAMDON>` with any secret you have or just random it!
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: webhook-token
  namespace: infra-jenkins
  labels:
    jenkins.io/credentials-type: secretText
  annotations:
    jenkins.io/credentials-description : "Webhook Token for Trigger Jobs"
type: Opaque
stringData:
  text: <SECURE_RAMDON>
```

Then, save as `webhook-token.yaml` file and run `kubectl -f webhook-token.yaml`

See more information at https://jenkinsci.github.io/kubernetes-credentials-provider-plugin/examples/

### How to add git token credentials into Jenkins

Replace `<TOKEN>` with Personal Access Token from Github
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: git-token-credentials
  namespace: infra-jenkins
  labels:
    jenkins.io/credentials-type: usernamePassword
  annotations:
    jenkins.io/credentials-description : "Git Credentials from Kubernetes"
type: Opaque
stringData:
  username: browol@github.com
  password: <TOKEN>
```

See more information at https://jenkinsci.github.io/kubernetes-credentials-provider-plugin/examples/

### How to add Github OAuth2 client secret

If you haven't create Github OAuth2 application, please go to https://github.com/settings/applications/new.

Create Kubernetes secret a file named `secret.yaml` from manifest as shown below:
**NOTE:** Replace `<BASE64ENCODED>` with Github OAuth2 client secret.
```yaml
apiVersion: v1
data:
  GITHUB_SECRET: <BASE64ENCODED>
kind: Secret
metadata:
  name: env-jenkins
type: Opaque
```

Then, run a command below to create a secret into `infra-jenkins` namespace.
```bash
kubectl apply -f secret.yaml -n infra-jenkins
```

### How to create docker private registry credential for push and pull

If you haven't issue token yet, please see the [official guideline](https://learn.microsoft.com/en-us/azure/container-registry/container-registry-authentication?tabs=azure-cli#az-acr-login-with---expose-token).

```bash
DOCKER_REGISTRY=$(kubectl create secret docker-registry docker-config \
--docker-server=acrdemo3fh45a.azurecr.io \
--docker-username=00000000-0000-0000-0000-000000000000 \
--docker-password=$ACCESS_TOKEN \
--save-config \
--dry-run=client \
-o yaml)
```

Decode the `.dockerconfigjson` key.
```bash
DOCKER_CONFIG=$(echo "${DOCKER_REGISTRY}" | yq '.data[".dockerconfigjson"]' | base64 -d)
```

Create/Update generic secret for docker config file
```bash
kubectl create secret generic docker-config \
--from-literal=config.json="$DOCKER_CONFIG" \
--namespace=infra-jenkins \
--save-config \
--dry-run=client \
-o yaml | kubectl apply -f -
```

## How to running locally
```bash
docker-compose up -d
```