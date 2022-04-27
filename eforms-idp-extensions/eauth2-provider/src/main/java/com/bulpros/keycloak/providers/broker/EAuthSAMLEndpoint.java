/*******************************************************************************
 * Copyright (c) 2022 Digitall Nature Bulgaria
 *
 * This program and the accompanying materials
 * are made available under the terms of the Apache License 2.0
 * which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Stefan Tabakov
 *    Nedka Taskova
 *    Stanimir Stoyanov
 *    Pavel Koev
 *    Igor Radomirov
 *******************************************************************************/
package com.bulpros.keycloak.providers.broker;

import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.IdentityProvider;
import org.keycloak.broker.saml.SAMLEndpoint;
import org.keycloak.broker.saml.SAMLIdentityProvider;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.dom.saml.v2.assertion.*;
import org.keycloak.dom.saml.v2.protocol.ResponseType;
import org.keycloak.events.Errors;
import org.keycloak.events.EventType;
import org.keycloak.models.KeyManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.protocol.saml.SamlPrincipalType;
import org.keycloak.saml.common.constants.JBossSAMLConstants;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.common.util.DocumentUtil;
import org.keycloak.saml.processing.core.saml.v2.common.SAMLDocumentHolder;
import org.keycloak.saml.processing.core.saml.v2.constants.X500SAMLProfileConstants;
import org.keycloak.saml.processing.core.saml.v2.util.AssertionUtil;
import org.keycloak.saml.validators.ConditionsValidator;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.services.ErrorPage;
import org.keycloak.services.messages.Messages;
import org.w3c.dom.Element;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.namespace.QName;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

public class EAuthSAMLEndpoint extends SAMLEndpoint {
    public final static String SAML_PROVIDER_ID= "SAML_PROVIDER_ID";
    @Context
    private KeycloakSession session;
    private final DestinationValidator destinationValidator;

    public EAuthSAMLEndpoint(RealmModel realm, SAMLIdentityProvider provider, SAMLIdentityProviderConfig config, IdentityProvider.AuthenticationCallback callback, DestinationValidator destinationValidator) {
        super(realm, provider, config, callback, destinationValidator);
        this.destinationValidator = destinationValidator;
    }

    @POST
    @Consumes({"application/x-www-form-urlencoded"})
    @Override
    public Response postBinding(@FormParam("SAMLRequest") String samlRequest, @FormParam("SAMLResponse") String samlResponse, @FormParam("RelayState") String relayState) {
        return (new EAuthSAMLEndpoint.EAuthPostBinding()).execute(samlRequest, samlResponse, relayState, (String)null);
    }

    protected class EAuthPostBinding extends SAMLEndpoint.PostBinding {
        protected EAuthPostBinding() {
            super();
        }

        @Override
        protected Response handleLoginResponse(String samlResponse, SAMLDocumentHolder holder, ResponseType responseType, String relayState, String clientId) {

            try {
                KeyManager.ActiveRsaKey keys = session.keys().getActiveRsaKey(realm);
                if (! isSuccessfulSamlResponse(responseType)) {
                    String statusMessage = responseType.getStatus() == null ? Messages.IDENTITY_PROVIDER_UNEXPECTED_ERROR : responseType.getStatus().getStatusMessage();
                    return callback.error(relayState, statusMessage);
                }
                if (responseType.getAssertions() == null || responseType.getAssertions().isEmpty()) {
                    return callback.error(relayState, Messages.IDENTITY_PROVIDER_UNEXPECTED_ERROR);
                }

                boolean assertionIsEncrypted = AssertionUtil.isAssertionEncrypted(responseType);

                if (config.isWantAssertionsEncrypted() && !assertionIsEncrypted) {
                    logger.error("The assertion is not encrypted, which is required.");
                    event.event(EventType.IDENTITY_PROVIDER_RESPONSE);
                    event.error(Errors.INVALID_SAML_RESPONSE);
                    return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.INVALID_REQUESTER);
                }

                Element assertionElement;

                if (assertionIsEncrypted) {
                    // This methods writes the parsed and decrypted assertion back on the responseType parameter:
                    assertionElement = AssertionUtil.decryptAssertion(holder, responseType, keys.getPrivateKey());
                } else {
                    /* We verify the assertion using original document to handle cases where the IdP
                    includes whitespace and/or newlines inside tags. */
                    assertionElement = DocumentUtil.getElement(holder.getSamlDocument(), new QName(JBossSAMLConstants.ASSERTION.get()));
                }

                boolean signed = AssertionUtil.isSignedElement(assertionElement);
                final boolean assertionSignatureNotExistsWhenRequired = config.isWantAssertionsSigned() && !signed;
                final boolean signatureNotValid = signed && config.isValidateSignature() && !AssertionUtil.isSignatureValid(assertionElement, getIDPKeyLocator());
                final boolean hasNoSignatureWhenRequired = ! signed && config.isValidateSignature() && ! containsUnencryptedSignature(holder);

                if (assertionSignatureNotExistsWhenRequired || signatureNotValid || hasNoSignatureWhenRequired) {
                    logger.error("validation failed");
                    event.event(EventType.IDENTITY_PROVIDER_RESPONSE);
                    event.error(Errors.INVALID_SIGNATURE);
                    return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.INVALID_REQUESTER);
                }

                AssertionType assertion = responseType.getAssertions().get(0).getAssertion();
                NameIDType subjectNameID = getSubjectNameID(assertion);
                String principal = getPrincipal(assertion);

                if (principal == null) {
                    logger.errorf("no principal in assertion; expected: %s", expectedPrincipalType());
                    event.event(EventType.IDENTITY_PROVIDER_RESPONSE);
                    event.error(Errors.INVALID_SAML_RESPONSE);
                    return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.INVALID_REQUESTER);
                }

                //Map<String, String> notes = new HashMap<>();
                BrokeredIdentityContext identity = new BrokeredIdentityContext(principal);
                identity.getContextData().put(SAML_LOGIN_RESPONSE, responseType);
                identity.getContextData().put(SAML_ASSERTION, assertion);
                if (clientId != null && ! clientId.trim().isEmpty()) {
                    identity.getContextData().put(SAML_IDP_INITIATED_CLIENT_ID, clientId);
                }

                identity.setUsername(principal);

                //SAML Spec 2.2.2 Format is optional
                if (subjectNameID != null && subjectNameID.getFormat() != null && subjectNameID.getFormat().toString().equals(JBossSAMLURIConstants.NAMEID_FORMAT_EMAIL.get())) {
                    identity.setEmail(subjectNameID.getValue());
                }

                if (config.isStoreToken()) {
                    identity.setToken(samlResponse);
                }

                ConditionsValidator.Builder cvb = new ConditionsValidator.Builder(assertion.getID(), assertion.getConditions(), destinationValidator)
                        .clockSkewInMillis(1000 * config.getAllowedClockSkew());
                try {
                    String issuerURL = getEntityId(session.getContext().getUri(), realm);
                    cvb.addAllowedAudience(URI.create(issuerURL));
                    // getDestination has been validated to match request URL already so it matches SAML endpoint
                    if (responseType.getDestination() != null) {
                        cvb.addAllowedAudience(URI.create(responseType.getDestination()));
                    }
                } catch (IllegalArgumentException ex) {
                    // warning has been already emitted in DeploymentBuilder
                }
                if (! cvb.build().isValid()) {
                    logger.error("Assertion expired.");
                    event.event(EventType.IDENTITY_PROVIDER_RESPONSE);
                    event.error(Errors.INVALID_SAML_RESPONSE);
                    return ErrorPage.error(session, null, Response.Status.BAD_REQUEST, Messages.EXPIRED_CODE);
                }

                AuthnStatementType authn = null;
                for (Object statement : assertion.getStatements()) {
                    if (statement instanceof AuthnStatementType) {
                        authn = (AuthnStatementType)statement;
                        identity.getContextData().put(SAML_AUTHN_STATEMENT, authn);
                        break;
                    }
                }
                if (assertion.getAttributeStatements() != null ) {
                    String email = getX500Attribute(assertion, X500SAMLProfileConstants.EMAIL);
                    if (email != null)
                        identity.setEmail(email);
                }

                String brokerUserId = config.getAlias() + "." + principal;
                identity.setBrokerUserId(brokerUserId);
                identity.setIdpConfig(config);
                identity.setIdp(provider);
                if (authn != null && authn.getSessionIndex() != null) {
                    identity.setBrokerSessionId(identity.getBrokerUserId() + "." + authn.getSessionIndex());
                }
                identity.setCode(relayState);

                try {
                    String providerId = holder.getSamlDocument().getDocumentElement()
                            .getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Subject")
                            .item(0).getTextContent();
                    logger.debug("EAuth Provider id: " + providerId);
                    var contextData = identity.getContextData();
                    if(!contextData.isEmpty()){
                        identity.getContextData().put("SAML_PROVIDER_ID", providerId);
                    }
                }
                catch (Exception e){
                    logger.warn("Could not read provider id from SAML response. Reason: " + e.getMessage());
                }

                return callback.authenticated(identity);
            } catch (WebApplicationException e) {
                return e.getResponse();
            } catch (Exception e) {
                throw new IdentityBrokerException("Could not process response from SAML identity provider.", e);
            }
        }

        private boolean isSuccessfulSamlResponse(ResponseType responseType) {
            return responseType != null && responseType.getStatus() != null && responseType.getStatus().getStatusCode() != null && responseType.getStatus().getStatusCode().getValue() != null && Objects.equals(responseType.getStatus().getStatusCode().getValue().toString(), JBossSAMLURIConstants.STATUS_SUCCESS.get());
        }

        private String expectedPrincipalType() {
            SamlPrincipalType principalType = EAuthSAMLEndpoint.this.config.getPrincipalType();
            switch(principalType) {
                case SUBJECT:
                    return principalType.name();
                case ATTRIBUTE:
                case FRIENDLY_ATTRIBUTE:
                    return String.format("%s(%s)", principalType.name(), EAuthSAMLEndpoint.this.config.getPrincipalAttribute());
                default:
                    return null;
            }
        }

        private NameIDType getSubjectNameID(AssertionType assertion) {
            SubjectType subject = assertion.getSubject();
            SubjectType.STSubType subType = subject.getSubType();
            return subType != null ? (NameIDType)subType.getBaseID() : null;
        }

        private String getX500Attribute(AssertionType assertion, X500SAMLProfileConstants attribute) {
            attribute.getClass();
            return this.getFirstMatchingAttribute(assertion, attribute::correspondsTo);
        }

        private String getFirstMatchingAttribute(AssertionType assertion, Predicate<AttributeType> predicate) {
            return assertion.getAttributeStatements().stream().map(AttributeStatementType::getAttributes).flatMap(Collection::stream).map(AttributeStatementType.ASTChoiceType::getAttribute).filter(predicate).map(AttributeType::getAttributeValue).flatMap(Collection::stream).findFirst().map(Object::toString).orElse((String) null);
        }

        private String getEntityId(UriInfo uriInfo, RealmModel realm) {
            String configEntityId = EAuthSAMLEndpoint.this.config.getEntityId();
            return configEntityId != null && !configEntityId.isEmpty() ? configEntityId : UriBuilder.fromUri(uriInfo.getBaseUri()).path("realms").path(realm.getName()).build(new Object[0]).toString();
        }

        private String getPrincipal(AssertionType assertion) {
            SamlPrincipalType principalType = EAuthSAMLEndpoint.this.config.getPrincipalType();
            if (principalType != null && !principalType.equals(SamlPrincipalType.SUBJECT)) {
                return principalType.equals(SamlPrincipalType.ATTRIBUTE) ? this.getAttributeByName(assertion, EAuthSAMLEndpoint.this.config.getPrincipalAttribute()) : this.getAttributeByFriendlyName(assertion, EAuthSAMLEndpoint.this.config.getPrincipalAttribute());
            } else {
                NameIDType subjectNameID = this.getSubjectNameID(assertion);
                return subjectNameID != null ? subjectNameID.getValue() : null;
            }
        }

        private String getAttributeByName(AssertionType assertion, String name) {
            return this.getFirstMatchingAttribute(assertion, (attribute) -> {
                return Objects.equals(attribute.getName(), name);
            });
        }

        private String getAttributeByFriendlyName(AssertionType assertion, String friendlyName) {
            return this.getFirstMatchingAttribute(assertion, (attribute) -> {
                return Objects.equals(attribute.getFriendlyName(), friendlyName);
            });
        }
    }
}
