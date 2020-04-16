# Fission error example

This repo has the code to reprocude an possible Fission / config error.

## Versions of used software

### Minikube
```
minikube version: v1.8.2
commit: eb13446e786c9ef70cb0a9f85a633194e62396a1
```

### kubectl
```
Client Version: version.Info{Major:"1", Minor:"18", GitVersion:"v1.18.1", GitCommit:"7879fc12a63337efff607952a323df90cdc7a335", GitTreeState:"clean", BuildDate:"2020-04-08T17:38:50Z", GoVersion:"go1.13.9", Compiler:"gc", Platform:"linux/amd64"}
Server Version: version.Info{Major:"1", Minor:"17", GitVersion:"v1.17.3", GitCommit:"06ad960bfd03b39c8310aaf92d1e7c12ce618213", GitTreeState:"clean", BuildDate:"2020-02-11T18:07:13Z", GoVersion:"go1.13.6", Compiler:"gc", Platform:"linux/amd64"}
```

### Virtualbox
```
Oracle VM VirtualBox VM Selector v6.1.4
```

### Fission

```
client:
  fission/core:
    BuildDate: "2020-02-03T08:40:57Z"
    GitCommit: bda974a72c9093e241c1dae6a7fc1a2d16e28b02
    Version: 1.8.0
server:
  fission/core:
    BuildDate: "2020-02-03T08:40:57Z"
    GitCommit: bda974a72c9093e241c1dae6a7fc1a2d16e28b02
    Version: 1.8.0
```

## Steps to reproduce

Run the following command

- `minikube start --driver=virtualbox --embed-certs=true --cpus=4 --memory=8192`

- `minikube addons enable ingress`

- `kubectl create ns fission`

- `helm install --namespace fission --name-template fission --set serviceType=NodePort,routerServiceType=NodePort https://github.com/fission/fission/releases/download/1.8.0/fission-all-1.8.0.tgz`

- `fission spec apply`

- In an HTTP client, go to [minikube ip]/test

The pod `newdeploy-opendata-ingestion-default-<hash>` in the `fission-function` namespace will keep terminating. Looking at the logs with the command ` kubectl logs newdeploy-opendata-ingestion-<hash> -n fission-function fetcher` will show the following error:

```json
{"level":"info","ts":1587046173.7135942,"caller":"app/server.go:136","msg":"fetcher ready to receive requests"}
{"level":"error","ts":1587046173.7220888,"logger":"fetcher","caller":"fetcher/fetcher.go:248","msg":"cannot fetch deployment: package build status was not \"succeeded\"","package_name":"opendata-ingestion","package_namespace":"default","package_build_status":"running","stacktrace":"github.com/fission/fission/pkg/fetcher.(*Fetcher).Fetch\n\t/go/src/pkg/fetcher/fetcher.go:248\ngithub.com/fission/fission/pkg/fetcher.(*Fetcher).SpecializePod\n\t/go/src/pkg/fetcher/fetcher.go:583\ngithub.com/fission/fission/cmd/fetcher/app.Run.func1\n\t/go/src/cmd/fetcher/app/server.go:104"}
{"level":"info","ts":1587046173.7221696,"logger":"fetcher","caller":"fetcher/fetcher.go:575","msg":"specialize request done","elapsed_time":0.00777645}
{"level":"fatal","ts":1587046173.7221825,"caller":"app/server.go:106","msg":"error specializing function pod","error":"error fetching deploy package: cannot fetch deployment: package build status was not \"succeeded\": pkg opendata-ingestion.default has a status of running","errorVerbose":"cannot fetch deployment: package build status was not \"succeeded\": pkg opendata-ingestion.default has a status of running\ngithub.com/fission/fission/pkg/fetcher.(*Fetcher).Fetch\n\t/go/src/pkg/fetcher/fetcher.go:252\ngithub.com/fission/fission/pkg/fetcher.(*Fetcher).SpecializePod\n\t/go/src/pkg/fetcher/fetcher.go:583\ngithub.com/fission/fission/cmd/fetcher/app.Run.func1\n\t/go/src/cmd/fetcher/app/server.go:104\nruntime.goexit\n\t/usr/local/go/src/runtime/asm_amd64.s:1337\nerror fetching deploy package\ngithub.com/fission/fission/pkg/fetcher.(*Fetcher).SpecializePod\n\t/go/src/pkg/fetcher/fetcher.go:585\ngithub.com/fission/fission/cmd/fetcher/app.Run.func1\n\t/go/src/cmd/fetcher/app/server.go:104\nruntime.goexit\n\t/usr/local/go/src/runtime/asm_amd64.s:1337","stacktrace":"github.com/fission/fission/cmd/fetcher/app.Run.func1\n\t/go/src/cmd/fetcher/app/server.go:106"}
```

Fission will return the following message with status code 502 to the client

```
error sending request to function
```
