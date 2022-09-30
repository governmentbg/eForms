# eforms-signature-services

**Description:**

Spring Boot application that uses DSS framework for e-signing. DSS framework is maintained by European Commission. The main code base of the framework could be found here: [dss](https://github.com/esig/dss)
This application is based on dss-demonstration web app which could be found here: [dss-demonstrations](https://github.com/esig/dss-demonstrations)

eforms-signature-services app supports CAdES detached signatures and could be used to sign digests of documents.
Additional signature forms could be implemented by utilizing DSS modules.

---

**Prerequisites:**

You need to fulfill the following prerequisites in order to start the application:
- JDK 11
- Maven
- DSS parent project and modules

DSS framework is modular and users could utilize just the modules required by their use cases. DSS maintainers don't provide Maven repository with pre-build DSS artifacts, thus for development purposes you could either clone and build the required modules from DSS repository or use eForms Nexus Maven registry. To access the registry contact eForms support team or [stefan.tabakov@bulpros.com](mailto:stefan.tabakov@bulpros.com).
With the successful build of required DSS modules, they go to your local .m2 repository and could be defined in eForms's pom file like regular dependencies. You won't be able to build eform-signature-service without having DSS modules successfully built.
---

