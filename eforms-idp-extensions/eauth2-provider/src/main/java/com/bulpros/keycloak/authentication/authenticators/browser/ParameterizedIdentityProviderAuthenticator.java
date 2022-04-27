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
        package com.bulpros.keycloak.authentication.authenticators.browser;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.keycloak.OAuth2Constants;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationProcessor;
import org.keycloak.authentication.Authenticator;
import org.keycloak.constants.AdapterConstants;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.protocol.oidc.OIDCLoginProtocol;
import org.keycloak.services.Urls;
import org.keycloak.services.managers.ClientSessionCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterizedIdentityProviderAuthenticator implements Authenticator {
    
    protected static final String ACCEPTS_PROMPT_NONE = "acceptsPromptNoneForwardFromClient";
    
    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (context.getUriInfo().getQueryParameters().containsKey(AdapterConstants.KC_IDP_HINT)) {
            String providerId = context.getUriInfo().getQueryParameters().getFirst(AdapterConstants.KC_IDP_HINT);
            if (providerId == null || providerId.equals("")) {
                log.trace("Skipping: kc_idp_hint query parameter is empty");
                context.attempted();
            } else {
                log.trace("Redirecting: %s set to %s", AdapterConstants.KC_IDP_HINT, providerId);
                redirect(context, providerId);
            }
        } else if (context.getAuthenticatorConfig() != null && context.getAuthenticatorConfig().getConfig().containsKey(ParameterizedIdentityProviderAuthenticatorFactory.DEFAULT_PROVIDER)) {
            String defaultProvider = context.getAuthenticatorConfig().getConfig().get(ParameterizedIdentityProviderAuthenticatorFactory.DEFAULT_PROVIDER);
            log.trace("Redirecting: default provider set to %s", defaultProvider);
            redirect(context, defaultProvider);
        } else {
            log.trace("No default provider set or %s query parameter provided", AdapterConstants.KC_IDP_HINT);
            context.attempted();
        }
        
    }
    
    protected void redirect(AuthenticationFlowContext context, String providerId) {
        Optional<IdentityProviderModel> idp = context.getRealm().getIdentityProvidersStream()
                .filter(IdentityProviderModel::isEnabled)
                .filter(identityProvider -> Objects.equals(providerId, identityProvider.getAlias()))
                .findFirst();
        if (idp.isPresent()) {
            String accessCode = new ClientSessionCode<>(context.getSession(), context.getRealm(), context.getAuthenticationSession()).getOrGenerateCode();
            String clientId = context.getAuthenticationSession().getClient().getClientId();
            String tabId = context.getAuthenticationSession().getTabId();
            URI location = Urls.identityProviderAuthnRequest(context.getUriInfo().getBaseUri(), providerId, context.getRealm().getName(), accessCode, clientId, tabId);
            MultivaluedMap<String, String> queryParameters = context.getUriInfo().getQueryParameters();
            UriBuilder uriBuilder = UriBuilder.fromUri(location);
            log.trace("Read query parameters");
            for (String key : queryParameters.keySet()) {
                Object value = queryParameters.getFirst(key);
                log.trace("Query parameter: " + key + " " + value);
                uriBuilder = uriBuilder.queryParam(key, value);
            }
            location = uriBuilder.build();
            if (context.getAuthenticationSession().getClientNote(OAuth2Constants.DISPLAY) != null) {
                location = UriBuilder.fromUri(location).queryParam(OAuth2Constants.DISPLAY, context.getAuthenticationSession().getClientNote(OAuth2Constants.DISPLAY)).build();
            }
            Response response = Response.seeOther(location)
                    .build();
            // will forward the request to the IDP with prompt=none if the IDP accepts forwards with prompt=none.
            if ("none".equals(context.getAuthenticationSession().getClientNote(OIDCLoginProtocol.PROMPT_PARAM)) &&
                    Boolean.valueOf(idp.get().getConfig().get(ACCEPTS_PROMPT_NONE))) {
                context.getAuthenticationSession().setAuthNote(AuthenticationProcessor.FORWARDED_PASSIVE_LOGIN, "true");
            }
            log.debug("Redirecting to %s", providerId);
            context.forceChallenge(response);
            return;
        }

        log.warn("Provider not found or not enabled for realm %s", providerId);
        context.attempted();
    }
    
    @Override
    public void close() {
        
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        
    }
}
