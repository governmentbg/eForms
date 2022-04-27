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

import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

public class EAuthIdentityProviderConfig extends SAMLIdentityProviderConfig {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public static final String SERVICE = "eauth2_service";
    public static final String PROVIDER = "eauth2_provider";
    public static final String ASSURANCE_LEVELS = "eauth2_assuranceLevels";
    public static final String INTEGRATIONS_URL = "eauth2_integrationsUrl";
    public static final String DEFAULT_ASSURANCE_LEVEL = "eauth2_defaultAssuranceLevel";
    public static final String REQUESTED_ASSURANCE_LEVEL = "eauth2_requestedAssuranceLevel";
    public static final String IDP_PROVIDERS_ASSURANCE_LEVEL_HIGH = "eauth2_providersHigh";
    public static final String IDP_PROVIDERS_ASSURANCE_LEVEL_SUBSTANTIAL = "eauth2_providersSubstantial";
    public static final String CITIZEN_ATTRIBUTES = "eauth2_citizenAttributes";
    public static final String REQUESTED_CITIZEN_ATTRIBUTES = "eauth2_requestedCitizenAttributes";
    public static final String[] PROTOCOL_TO_PORT_MAP = new String[] { "http=80", "https=443", "https=44310" };


    EAuthIdentityProviderConfig(IdentityProviderModel identityProviderModel) {
        super(identityProviderModel);
    }

    EAuthIdentityProviderConfig() {
        super();
    }

    public String getService() {
        return getConfig().get(SERVICE);
    }

    public String getProvider() {
        return getConfig().get(PROVIDER);
    }

    public void addAssuranceLevel(String assuranceLevel) {
        String levels = getConfig().get(ASSURANCE_LEVELS);
        if (levels == null || levels.isEmpty()) {
            getConfig().put(ASSURANCE_LEVELS, assuranceLevel);
        } else {
            getConfig().put(ASSURANCE_LEVELS, levels + "," + assuranceLevel);
        }
    }

    public String getDefaultAssuranceLevel() {
        return getConfig().get(DEFAULT_ASSURANCE_LEVEL);
    }

    public void addCitizenAttribute(String citizenAttribute) {
        String citizen = getConfig().get(CITIZEN_ATTRIBUTES);
        if (citizen == null || citizen.isEmpty()) {
            getConfig().put(CITIZEN_ATTRIBUTES, citizenAttribute);
        } else {
            getConfig().put(CITIZEN_ATTRIBUTES, citizen + "," + citizenAttribute);
        }
    }

    public String[] getRequestedCitizenAttributes() {
        String citizen = getConfig().get(REQUESTED_CITIZEN_ATTRIBUTES);
        if (citizen == null || citizen.isEmpty()) {
            return new String[]{};
        }
        return citizen.split(",");
    }

    public String getAttributeFriendlyName(String attribute) {
        String[] attributeSplit = attribute.split(":");
        return attributeSplit[attributeSplit.length - 1];
    }
    
    public String getValueByName(String name) {
        return getConfig().get(name);
    }
}
