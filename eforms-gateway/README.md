# eforms-gateway

**Description:**

Spring Boot application that is serving as API gateway and is secured with KeyCloak using OAuth 2.0.

---

**Prerequisites:**

You need to fulfill the following prerequisites in order to start the application:
- JDK 11
- Maven
- KeyCloak service running at http://localhost:9090

---

 **How to start the application locally:**

   1. Login at KeyCloak Admin Console (http://localhost:9090/auth/admin/master/console).
   2. Select "Camunda" realm.
   3. Import the configuration for API gateway client (/eforms-gateway/src/main/resources/keycloak/eforms-gateway.json) into Keycloak. As a result new client will be created - "eforms-gateway".
   4. Click on "Clients" and select "eforms-gateway". In "Credentials" section click "Regenerate secret".
   5. Update the configuration properties with the newly generated client secret.

```
	##
	# KeyCloak Security Configuration
	##
	keycloak.url.auth: ${KEYCLOAK_URL_AUTH:http://localhost:9090}
	keycloak.url.token: ${KEYCLOAK_URL_TOKEN:http://localhost:9090}
	keycloak.url.plugin: ${KEYCLOAK_URL_PLUGIN:http://localhost:9090}
	keycloak.client.id: ${KEYCLOAK_CLIENT_ID:eforms-gateway}
	keycloak.client.secret: ${KEYCLOAK_CLIENT_SECRET:<new_secret>}
```