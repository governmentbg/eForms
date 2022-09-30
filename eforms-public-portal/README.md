# Prerequests

[Angular CLI](https://github.com/angular/angular-cli) installed globally..

Have installed and added to PATH [Python](https://www.python.org/downloads/)

Have installed globaly windows-build-tools (npm install --global windows-build-tools)

Create an user in keyclock.

Add Form.io Premium components registry with the following commands(for Docker):

1. 
```
FORMIO_REGISTRY=pkg.form.io && FORMIO_REGISTRY_USER=<user> && FORMIO_REGISTRY_PASSWORD=<password>
```
2. 
```
TOKEN=curl -s \                
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
  -X PUT --data "{\"name\": \"$FORMIO_REGISTRY_USER\", \"password\":\"$FORMIO_REGISTRY_PASSWORD\"}" \
  --user $FORMIO_REGISTRY_USER:$FORMIO_REGISTRY_PASSWORD \
  https://$FORMIO_REGISTRY/-/user/org.couchdb.user:$FORMIO_REGISTERY_USER 2>&1 | \
  grep token | awk -F'[:}]' '{print $2}'
```
3. 
```
npm config set registry https://$FORMIO_REGISTRY
```
4. 
```
npm set //$FORMIO_REGISTRY/:_authToken $TOKEN
```
5. 
```
npm install @formio/premium --registry https://$FORMIO_REGISTRY
```


Add Form.io Premium components registry with the following commands(for local server):

1. 
```
npm login --registry https://pkg.form.io
Enter username : <username>
Enter password : <password>
Enter email : <email>
```
2. 
```
npm install @formio/premium --registry https://pkg.form.io 
```
## Initial setup

Navigate to your cloned repo.
Run `npm install` for a dev server.

## Run server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.
You can add `--configuration=` to build the dev,test,stage or prod enviroment specificly.

## Docker setup

Run ` docker-compose up --build <service-name>`. Available services: test, dev, stage, prod.

