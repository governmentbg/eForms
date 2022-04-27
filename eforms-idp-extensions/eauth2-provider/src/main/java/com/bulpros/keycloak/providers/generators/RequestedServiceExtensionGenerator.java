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
package com.bulpros.keycloak.providers.generators;

import java.util.Map;

import javax.xml.stream.XMLStreamWriter;

import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.models.KeycloakSession;
import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import org.keycloak.saml.common.exceptions.ProcessingException;
import org.keycloak.saml.common.util.StaxUtil;

import com.bulpros.keycloak.providers.EAuthIdentityProviderConfig;

public class RequestedServiceExtensionGenerator implements SamlProtocolExtensionsAwareBuilder.NodeGenerator {
	
    public static final String NS_URI = "urn:bg:egov:eauth:2.0:saml:ext";

    public static final String NS_PREFIX = "egovbga";

    private final EAuthIdentityProviderConfig eAuthIdentityProviderConfig;
    
    protected final KeycloakSession session;
    
    
    public RequestedServiceExtensionGenerator(SAMLIdentityProviderConfig samlIdentityProviderConfig, KeycloakSession session) {
        super();
        this.session = session;
        this.eAuthIdentityProviderConfig = (EAuthIdentityProviderConfig) samlIdentityProviderConfig;
    }

    @Override
    public void write(XMLStreamWriter writer) throws ProcessingException {
        StaxUtil.writeStartElement(writer, NS_PREFIX, "RequestedService", NS_URI);
        StaxUtil.writeNameSpace(writer, NS_PREFIX, NS_URI);

        StaxUtil.writeStartElement(writer, NS_PREFIX, "Service", "");
        StaxUtil.writeCharacters(writer, eAuthIdentityProviderConfig.getService());
        StaxUtil.writeEndElement(writer);
        
        StaxUtil.writeStartElement(writer, NS_PREFIX, "Provider", "");
        StaxUtil.writeCharacters(writer, eAuthIdentityProviderConfig.getProvider());
        StaxUtil.writeEndElement(writer);
        
        StaxUtil.writeStartElement(writer, NS_PREFIX, "LevelOfAssurance", "");
        Map<String, String> userSessionNotes = session.getContext().getAuthenticationSession().getUserSessionNotes();
        String assuranceLevel = userSessionNotes.get(EAuthIdentityProviderConfig.REQUESTED_ASSURANCE_LEVEL);
        if (assuranceLevel == null) {
            assuranceLevel = eAuthIdentityProviderConfig.getDefaultAssuranceLevel();
        }
        StaxUtil.writeCharacters(writer, assuranceLevel);
        StaxUtil.writeEndElement(writer);
        
        StaxUtil.writeEndElement(writer);
        
        StaxUtil.flush(writer);
    }
    
    

}
