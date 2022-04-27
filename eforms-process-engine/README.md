# eForms-process-engine

**Description:** 

Spring Boot application using the Keycloak Identity Provider Plugin in combination with the OAuth 2.0. 
The service integrated Camunda BPMN engine.  

**Prerequisites:**

  In order to run the project as Spring Boot App, you will need JDK 11, Maven.


 **How to start an application locally:**
 
   1. The procedure starts with clone eForms-provisioning. In README.md are described necessary steps.
   	At the end of the process, we have started locally a few services. In this case, we need Keycloak service at http://localhost:9090;
   2. Import into Keycloak file /eForms-process-engine/src/main/resources/keycloak/realm-camunda.json. That's will create realm, roles and clients.
   More about Keycloak configuration read here: [SSO Camunda](https://github.com/camunda/camunda-bpm-identity-keycloak)
   3. Login at the Keycloak Admin Console http://localhost:9090/auth/admin/master/console using admin/admin.
   4. Select Camunda realm.
   5. Click on clients and select camunda-identity-service. In ''credentials'' tab generate client secret.
   6. Configure application.yaml file.
 
```
	#### Externalized Keycloak configuration
	keycloak:
		\# SSO Authentication requests. Send by application as redirect to the browser
		url.auth: ${KEYCLOAK_URL_AUTH:http://localhost:9090}<br>
		\# SSO Token requests. Send from the application to Keycloak
		url.token: ${KEYCLOAK_URL_TOKEN:http://localhost:9090}
		\# Keycloak access for the Identity Provider plugin.
		url.plugin: ${KEYCLOAK_URL_PLUGIN:http://localhost:9090}

	#### Keycloak Camunda Identity Client
		client.id: ${KEYCLOAK_CLIENT_ID:camunda-identity-service}
		client.secret: ${KEYCLOAK_CLIENT_SECRET:xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx}
```
   
   7. Optionally for test purpose in Keycloak Admin Console create a user to logged in.
	

