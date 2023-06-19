# Deploy on Kubernetes cluster

## How to deploy using Kustomize
```bash
kustomize build . | kubectl apply -f -
```

## Create a ClusterRole and Rolebinding to enable Jenkins service account access to resources in the nginx namespace
```bash
kubectl apply -f jenkins-rbac.yaml -n nginx
```

Now, you can use cURL command to test your connection as shown below:
```bash
IP_ADDR=20.195.116.80
curl -i http://nginx.com/ --resolve nginx.com:80:$IP_ADDR -H "Host: nginx.com"
```
