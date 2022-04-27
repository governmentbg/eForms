# eauth2-provider

- [eauth2-provider](#eauth2-provider)
  - [Features](#features)
  - [Compatibility](#compatibility)
  - [Installation](#installation)
  - [How to use it](#how-to-use-it)
    - [Requirements](#requirements)
    - [Configuration](#configuration)
      - [Mappers](#mappers)
- [bg-theme](#theme)

This [Keycloak](https://www.keycloak.org) plugin adds an identity provider allowing to use [eAuthentication](https://e-gov.bg/wps/portal/agency/systems/info-systems/e-authentication) services.

## Features

* Extension of default Keycloak's SAML provider features and configuration screen with eAuthentication specific extensions
* Service OID and Provider OID configuration in Keycloak admin console
* Default assurance level (in case none is requested)
* Mapping of identity providers (Evrotrust, Borica, etc. supported by eAuthentication) to an assurance level
* Fallback that puts the requested assurance level to user notes in case the above mapping fails or is not configured
* Configuration of attributes which should be added to AuthnRequest requests
* Logging of events to Auditlog external service

## Compatibility

* The version 1.0.0 of this plugin is compatible with Keycloak `12.0.4`. 

## Installation

The plugin installation is simple and can be done without a Keycloak server restart.

* Download the latest release
* Copy the EAR file into the `standalone/deployments` directory in your Keycloak server's root
* Restart Keycloak (optional, hot deployment should work)

You can also clone the Git Repository and install the plugin locally with the following command:

```
$ mvn clean install wildfly:deploy
```

## How to use it

### Requirements

You must follow the [eAuthentication integration guidelines](https://e-gov.bg/wps/portal/agency/about-us/administration-service/info-administrations/info-integration/e-auth?contentIDR=93cd0a7a-2f73-42a3-b697-5acb1e620516&useDefaultText=0&useDefaultDesc=0) to retrieve plugin configuration information (Service OID, Provider OID, ...)

There are 2 environments, `Integration` and `Production`. More information about the integration within the documents from the above link.

### Configuration

Once the installation is complete, the `eAuthentication v2.0` identity provider appears. Once added, you can see the following configuration page:

![eauth2-provider](/assets/eauth2-conf-provider.png)

You should import eAuthentication metadata for the environment you'd like to use, so that the available Citizen Attributes and Single Sign-On Service URL are loaded in the configuration.
The metadata for `Integration` environment is [here](https://eauth-test.egov.bg/tfauthbe/saml/metadata/idp)
The metadata for `Production` environment is [here](https://eauth.egov.bg/tfauthbe/saml/metadata/idp)

Enter your `Service OID` and `Provider OID`, requested.
Set `Principal Type` to `Attribute [Name]`. Set `Principal Attribute` to `urn:egov:bg:eauth:2.0:attributes:personIdentifier`
For `Production` environment set `Service Provider Entity ID` to the url of your Keycloak metadata

The configured alias (`eauth2`) could be added to `Identity Provider Redirector` in the Authentication flows, so that the default Keycloak login screen is not shown.

#### Mappers

Once the configuration validated, you can add the mappers needed to retrieve the attributes you want from eAuthentication.
The main mappers are automatically added when creating the identity provider.

Mappers examples:
* Name : `Person Identifier Mapper`, Mapper Type : `Attribute Importer`, Attribute Name : `urn:egov:bg:eauth:2.0:attributes:personIdentifier`, User Attribute Name : `personIdentifier`
* Name : `Fullname Mapper`, Mapper Type : `eAuthentication User Mapper`, Attribute Name: `urn:egov:bg:eauth:2.0:attributes:personName`, Attribute Friendly Name : `personName`
* Name : `Email to Username Mapper`, Mapper Type : `Username Template Importer`, Template : `${ATTRIBUTE.urn:egov:bg:eauth:2.0:attributes:email}`, Target : `BROKER_USERNAME`
* Name: `Person name to Full name Mapper`, Mapper Type : `Attribute Importer`, Attribute Name : `urn:egov:bg:eauth:2.0:attributes:personName`, User Attribute Name : `fullName`
* Name: `Email Mapper`, Mapper Type : `Attribute Importer`, Attribute Name : `urn:egov:bg:eauth:2.0:attributes:email`, User Attribute Name : `email`

# Theme

This extension provides 2 themes:
* `eauth2`
* `bg-theme`

eauth2 is based on bg-theme, so eauth2 could not be used without having bg-theme installed. That's why both themes are packaged in a single EAR file. You should set eauth2 theme to the master realm in order to see the specific eAuthentication configuration in Keycloak admin console.
bg-theme is based on keycloak theme and provides translations of Keycloak labels and texts to Bulgarian language
Choose your theme and go to the following url: `https://<keycloak-url>/auth/realms/<realm>/account`
