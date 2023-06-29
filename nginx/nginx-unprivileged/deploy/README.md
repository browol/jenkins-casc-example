# Deploy on Kubernetes cluster

## How to deploy using Kustomize
```bash
kustomize build . | kubectl apply -f -
```

Now, you can use cURL command to test your connection as shown below:
```bash
IP_ADDR=20.198.157.0
curl -i http://nginx.com/ --resolve nginx.com:80:$IP_ADDR -H "Host: nginx.com"
```
