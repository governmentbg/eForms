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
package com.bulpros.keycloak.providers;

import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.auditlog.repository.client.AuditlogClient;
import com.bulpros.keycloak.providers.broker.EAuthSAMLEndpoint;
import com.bulpros.keycloak.providers.generators.CitizenAttributesExtensionGenerator;
import com.bulpros.keycloak.providers.generators.RequestedServiceExtensionGenerator;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.saml.SAMLIdentityProvider;
import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.dom.saml.v2.protocol.AuthnRequestType;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeyManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.protocol.saml.JaxrsSAML2BindingBuilder;
import org.keycloak.protocol.saml.SamlSessionUtils;
import org.keycloak.protocol.saml.preprocessor.SamlAuthenticationPreprocessor;
import org.keycloak.saml.SAML2AuthnRequestBuilder;
import org.keycloak.saml.SAML2NameIDPolicyBuilder;
import org.keycloak.saml.SAML2RequestedAuthnContextBuilder;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.processing.core.util.KeycloakKeySamlExtensionGenerator;
import org.keycloak.saml.validators.DestinationValidator;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.util.JsonSerialization;
import org.w3c.dom.Document;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

@Slf4j
public class EAuthIdentityProvider extends SAMLIdentityProvider {

    private static final String USER_SESSION_NOTES = "userSessionNotes";
    private static final String CLIENT_NOTES_PARAMETER_PREFIX = "client_request_param";
    private final DestinationValidator destinationValidator;
    private AuditlogClient.Builder builder;
    private List<String> idpProvidersHigh = new ArrayList<>();
    private List<String> idpProvidersSubstantial = new ArrayList<>();

    public EAuthIdentityProvider(KeycloakSession session, SAMLIdentityProviderConfig config,
            DestinationValidator destinationValidator) {
        super(session, config, destinationValidator);
        this.destinationValidator = destinationValidator;
        loadConfiguration();
    }

    protected void loadConfiguration() {
        try {
            String integrationsUrl = this.getConfig().getConfig().get(EAuthIdentityProviderConfig.INTEGRATIONS_URL);
            this.builder =  new AuditlogClient.Builder(UriBuilder.fromUri(integrationsUrl).build());
        } catch (Exception e){
            logger.warn("Auditlog service is not configured!");
        }
        String configHighString  = this.getConfig().getConfig()
                .get(EAuthIdentityProviderConfig.IDP_PROVIDERS_ASSURANCE_LEVEL_HIGH);
        if(Objects.nonNull(configHighString)) {
            idpProvidersHigh = Arrays.asList(configHighString.split("\\s*,\\s*"));
        }
        String configSubstantialString = this.getConfig().getConfig()
                .get(EAuthIdentityProviderConfig.IDP_PROVIDERS_ASSURANCE_LEVEL_SUBSTANTIAL);
        if(Objects.nonNull(configSubstantialString)) {
            idpProvidersSubstantial = Arrays.asList(configSubstantialString.split("\\s*,\\s*"));
        }
    }

    @Override
    public Object callback(RealmModel realm, AuthenticationCallback callback, EventBuilder event) {
        logEvent(EventTypeEnum.AIS_AUTHN_RECEIVED, "Authentication Response is received from identity broker");
        return new EAuthSAMLEndpoint(realm, this, getConfig(), callback, destinationValidator);
    }

    @Override
    public Response performLogin(AuthenticationRequest request) {
        log.trace("performLogin");
        try {
            Map<String, String> requestClientsNotes  = getClientNotesParameters(request);
            Map<String, String> requestUriParameters = getUriParameters(request);
            RealmModel realm = request.getRealm();
            String issuerURL = getEntityId(request.getUriInfo(), realm);
            String destinationUrl = getConfig().getSingleSignOnServiceUrl();
            String nameIDPolicyFormat = getConfig().getNameIDPolicyFormat();
            
            addToSession(requestClientsNotes);
            addToSession(requestUriParameters);

            if (nameIDPolicyFormat == null) {
                nameIDPolicyFormat = JBossSAMLURIConstants.NAMEID_FORMAT_PERSISTENT.get();
            }

            String protocolBinding = JBossSAMLURIConstants.SAML_HTTP_REDIRECT_BINDING.get();

            String assertionConsumerServiceUrl = request.getRedirectUri();

            if (getConfig().isPostBindingResponse()) {
                protocolBinding = JBossSAMLURIConstants.SAML_HTTP_POST_BINDING.get();
            }

            SAML2RequestedAuthnContextBuilder requestedAuthnContext = new SAML2RequestedAuthnContextBuilder()
                    .setComparison(getConfig().getAuthnContextComparisonType());

            for (String authnContextClassRef : getAuthnContextClassRefUris())
                requestedAuthnContext.addAuthnContextClassRef(authnContextClassRef);

            for (String authnContextDeclRef : getAuthnContextDeclRefUris())
                requestedAuthnContext.addAuthnContextDeclRef(authnContextDeclRef);

            String loginHint = getConfig().isLoginHint()
                    ? request.getAuthenticationSession().getClientNote(OIDCLoginProtocol.LOGIN_HINT_PARAM)
                    : null;
            SAML2AuthnRequestBuilder authnRequestBuilder = new SAML2AuthnRequestBuilder()
                    .assertionConsumerUrl(assertionConsumerServiceUrl).isPassive(false).destination(destinationUrl)
                    .issuer(issuerURL).forceAuthn(getConfig().isForceAuthn()).protocolBinding(protocolBinding)
                    .nameIdPolicy(SAML2NameIDPolicyBuilder.format(nameIDPolicyFormat).setAllowCreate(Boolean.TRUE))
                    .requestedAuthnContext(requestedAuthnContext).subject(loginHint)
                    .addExtension(new RequestedServiceExtensionGenerator(getConfig(), session))
                    .addExtension(new CitizenAttributesExtensionGenerator(getConfig()));

            JaxrsSAML2BindingBuilder binding = new JaxrsSAML2BindingBuilder(session)
                    .relayState(request.getState().getEncoded());
            boolean postBinding = getConfig().isPostBindingAuthnRequest();

            if (getConfig().isWantAuthnRequestsSigned()) {
                KeyManager.ActiveRsaKey keys = session.keys().getActiveRsaKey(realm);

                String keyName = getConfig().getXmlSigKeyInfoKeyNameTransformer().getKeyName(keys.getKid(),
                        keys.getCertificate());
                binding.signWith(keyName, keys.getPrivateKey(), keys.getPublicKey(), keys.getCertificate())
                        .signatureAlgorithm(getSignatureAlgorithm()).signDocument();
                if (!postBinding && getConfig().isAddExtensionsElementWithKeyInfo()) { // Only include extension if
                                                                                       // REDIRECT binding and signing
                                                                                       // whole SAML protocol message
                    authnRequestBuilder.addExtension(new KeycloakKeySamlExtensionGenerator(keyName));
                }
            }

            AuthnRequestType authnRequest = authnRequestBuilder.createAuthnRequest();

            Document doc = authnRequestBuilder.toDocument();

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            log.trace("XML IN String format is: \n" + writer.toString());

            for (Iterator<SamlAuthenticationPreprocessor> it = SamlSessionUtils
                    .getSamlAuthenticationPreprocessorIterator(session); it.hasNext();) {
                authnRequest = it.next().beforeSendingLoginRequest(authnRequest, request.getAuthenticationSession());
            }

            if (authnRequest.getDestination() != null) {
                destinationUrl = authnRequest.getDestination().toString();
            }
            
            logEvent(EventTypeEnum.PORTAL_AUTHN_REQUEST_SENT,
                    "Request for authentication is sent from public portal");

            if (postBinding) {
                return binding.postBinding(authnRequestBuilder.toDocument()).request(destinationUrl);
            } else {
                return binding.redirectBinding(authnRequestBuilder.toDocument()).request(destinationUrl);
            }
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not create authentication request.", e);
        }
    }

    @Override
    public void updateBrokeredUser(KeycloakSession session, RealmModel realm, UserModel user, BrokeredIdentityContext context) {
        var authenticationSession = session.getContext().getAuthenticationSession();
        if (authenticationSession != null) {
            var notes = authenticationSession.getUserSessionNotes();
            if(notes.isEmpty() || !notes.containsKey(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL)){
                String samlProviderId = (String) context.getContextData().get(EAuthSAMLEndpoint.SAML_PROVIDER_ID);
                AssuranceLevelEnum assuranceLevel = getAssuranceLevelByProviderId(samlProviderId);
                authenticationSession.setUserSessionNote(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL, assuranceLevel.name());
            }
        }
        if (context.getEmail() != null && !context.getEmail().isEmpty()) {
            user.setEmail(context.getEmail());
            user.setUsername(context.getEmail());
        }
    }

    @Override
    public void preprocessFederatedIdentity(KeycloakSession session, RealmModel realm, BrokeredIdentityContext context) {
        var authenticationSession = session.getContext().getAuthenticationSession();
        if (authenticationSession != null){
            var notes = authenticationSession.getUserSessionNotes();
            var fulfilledNotes = this.determineAssuranceLevelIfNotProvided(notes, context);
            var contextData = context.getContextData();
            if(!contextData.isEmpty()){
                context.getContextData().put(USER_SESSION_NOTES, fulfilledNotes);
            }
        }
    }

    private Map<String, String> determineAssuranceLevelIfNotProvided(Map<String, String> userSessionNotes, BrokeredIdentityContext context) {
        if(userSessionNotes.isEmpty() || !userSessionNotes.containsKey(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL)){
            String samlProviderId = "";
            try {
                samlProviderId = (String) context.getContextData().get(EAuthSAMLEndpoint.SAML_PROVIDER_ID);
                AssuranceLevelEnum assuranceLevel = this.getAssuranceLevelByProviderId(samlProviderId);
                userSessionNotes.put(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL, assuranceLevel.name());
            } catch (Exception exception) {
                logger.warn("Context doesn't contain provider id.");
            }
            return userSessionNotes;
        }
        userSessionNotes.put(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL, AssuranceLevelEnum.LOW.name());
        return userSessionNotes;
    }

    private  AssuranceLevelEnum getAssuranceLevelByProviderId(String providerId) {
        if(!Objects.nonNull(providerId)  || providerId.isEmpty()){
            return AssuranceLevelEnum.LOW;
        }
        if(!idpProvidersHigh.isEmpty() && idpProvidersHigh.contains(providerId)) {
            return AssuranceLevelEnum.HIGH;
        }
        if(!idpProvidersSubstantial.isEmpty() && idpProvidersSubstantial.contains(providerId)) {
            return AssuranceLevelEnum.SUBSTANTIAL;
        }
        return AssuranceLevelEnum.LOW;
    }

    @Override
    public void importNewUser(KeycloakSession session, RealmModel realm, UserModel user, BrokeredIdentityContext context) {
        var contextData = context.getContextData();

        if(!contextData.isEmpty() && contextData.containsKey(USER_SESSION_NOTES)){
            var authenticationSession = session.getContext().getAuthenticationSession();
            if(authenticationSession != null){
                var sessionNotes = (Map<String, String>) contextData.get(USER_SESSION_NOTES);
                sessionNotes.forEach( (k,v) -> authenticationSession.setUserSessionNote(k, v));
            }
        }

        user.setUsername(context.getEmail());
        log.debug(realm.toString());
        realm.setEditUsernameAllowed(false);
    }

    @Override
    public void authenticationFinished(AuthenticationSessionModel authSession, BrokeredIdentityContext context) {
        super.authenticationFinished(authSession,context);
        logEvent(EventTypeEnum.PORTAL_AUTHN_REQUEST_SENT, "Redirect back to public portal");
    }

    private Map<String, String> getClientNotesParameters(AuthenticationRequest request){
        var clientsNotes = request.getAuthenticationSession().getClientNotes();
        final Map<String, String> clientsNotesWithoutPrefix = new HashMap<>();
        clientsNotes.entrySet().removeIf(key -> !key.getKey().startsWith(CLIENT_NOTES_PARAMETER_PREFIX));
        for (Map.Entry<String, String> entry: clientsNotes.entrySet()) {
            log.trace("Client Request parameter: " + entry.getKey() + " " + entry.getValue());
            String key = entry.getKey().replace(CLIENT_NOTES_PARAMETER_PREFIX + "_", "");
            clientsNotesWithoutPrefix.put(key, entry.getValue());
        }
        return clientsNotesWithoutPrefix;
    }

    private void logEvent(EventTypeEnum eventTypeEnum, String description) {
        try {
            AuditlogClient auditlogClient = this.builder
                    .eventTime(Calendar.getInstance().getTime())
                    .eventType(eventTypeEnum)
                    .eventDescription(description)
                    .build();
            auditlogClient.getResponse();
        } catch (Exception exception) {
            log.error("Could not log message in auditlog! Reason: " + exception.getMessage());
        }
    }

    private Map<String, String> getUriParameters(AuthenticationRequest request) {
        UriInfo uriInfo = request.getUriInfo();
        final MultivaluedMap<String, String> requestParameters = uriInfo.getQueryParameters();
        Map<String,String> parameters = new HashMap<>();
        for (String key : requestParameters.keySet()) {
            String value = requestParameters.getFirst(key);
            parameters.put(key, value);
            log.trace("URI Request parameter: " + key + " " + value);
        }
        return parameters;
    }

    private void addToSession(Map<String, String> parameters) {
        for (Map.Entry<String, String> entry: parameters.entrySet()) {
            log.trace("Client Request parameter: " + entry.getKey() + " " + entry.getValue());
            String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, entry.getKey());
            session
                .getContext()
                .getAuthenticationSession()
                .setUserSessionNote(EAuthIdentityProviderFactory.PROVIDER_ID + "_" + key, entry.getValue());
        }
    }
    
    private String getEntityId(UriInfo uriInfo, RealmModel realm) {
        String configEntityId = getConfig().getEntityId();

        if (configEntityId == null || configEntityId.isEmpty())
            return UriBuilder.fromUri(uriInfo.getBaseUri()).path("realms").path(realm.getName()).build().toString();
        else
            return configEntityId;
    }

    private List<String> getAuthnContextClassRefUris() {
        String authnContextClassRefs = getConfig().getAuthnContextClassRefs();
        if (authnContextClassRefs == null || authnContextClassRefs.isEmpty())
            return new LinkedList<String>();

        try {
            return Arrays.asList(JsonSerialization.readValue(authnContextClassRefs, String[].class));
        } catch (Exception e) {
            logger.warn("Could not json-deserialize AuthContextClassRefs config entry: " + authnContextClassRefs, e);
            return new LinkedList<String>();
        }
    }

    private List<String> getAuthnContextDeclRefUris() {
        String authnContextDeclRefs = getConfig().getAuthnContextDeclRefs();
        if (authnContextDeclRefs == null || authnContextDeclRefs.isEmpty())
            return new LinkedList<String>();

        try {
            return Arrays.asList(JsonSerialization.readValue(authnContextDeclRefs, String[].class));
        } catch (Exception e) {
            logger.warn("Could not json-deserialize AuthContextDeclRefs config entry: " + authnContextDeclRefs, e);
            return new LinkedList<String>();
        }
    }
}
