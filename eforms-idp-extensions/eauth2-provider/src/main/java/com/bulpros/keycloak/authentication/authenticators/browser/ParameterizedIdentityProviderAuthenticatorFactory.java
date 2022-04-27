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

import static org.keycloak.provider.ProviderConfigProperty.STRING_TYPE;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.Config.Scope;
import org.keycloak.OAuth2Constants;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.DisplayTypeAuthenticatorFactory;
import org.keycloak.authentication.authenticators.AttemptedAuthenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

public class ParameterizedIdentityProviderAuthenticatorFactory
        implements AuthenticatorFactory, DisplayTypeAuthenticatorFactory, ServerInfoAwareProviderFactory {
    
    protected static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.ALTERNATIVE, AuthenticationExecutionModel.Requirement.DISABLED
    };
    
    protected static final String DEFAULT_PROVIDER = "defaultProvider";

    @Override
    public Authenticator create(KeycloakSession session) {
        return new ParameterizedIdentityProviderAuthenticator();
    }

    @Override
    public void init(Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "params-identity-provider-redirector";
    }

    @Override
    public String getDisplayType() {
        return "Parameterized Identity Provider Redirector";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Redirects to default Identity Provider or Identity Provider specified with kc_idp_hint and other query parameter";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty rep = new ProviderConfigProperty(DEFAULT_PROVIDER, "Default Identity Provider", "To automatically redirect to an identity provider set to the alias of the identity provider", STRING_TYPE, null);
        return Collections.singletonList(rep);
    }

    @Override
    public Authenticator createDisplay(KeycloakSession session, String displayType) {
        if (displayType == null) return new ParameterizedIdentityProviderAuthenticator();
        if (!OAuth2Constants.DISPLAY_CONSOLE.equalsIgnoreCase(displayType)) return null;
        return AttemptedAuthenticator.SINGLETON;  // ignore this authenticator
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        Map<String, String> ret = new LinkedHashMap<>();
        ret.put("details", "Used to pass parameters on redirect");
        return ret;
    }

}
