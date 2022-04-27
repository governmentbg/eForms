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

import org.keycloak.broker.saml.SAMLIdentityProviderConfig;
import org.keycloak.saml.SamlProtocolExtensionsAwareBuilder;
import org.keycloak.saml.common.exceptions.ProcessingException;
import org.keycloak.saml.common.util.StaxUtil;

import com.bulpros.keycloak.providers.EAuthIdentityProviderConfig;

import javax.xml.stream.XMLStreamWriter;

public class CitizenAttributesExtensionGenerator implements SamlProtocolExtensionsAwareBuilder.NodeGenerator {

    public static final String NS_URI = "urn:bg:egov:eauth:2.0:saml:ext";

    public static final String NS_PREFIX = "egovbga";

    private final EAuthIdentityProviderConfig eAuthIdentityProviderConfig;

    public CitizenAttributesExtensionGenerator(SAMLIdentityProviderConfig samlIdentityProviderConfig) {
        super();
        this.eAuthIdentityProviderConfig = (EAuthIdentityProviderConfig) samlIdentityProviderConfig;
    }

    @Override
    public void write(XMLStreamWriter writer) throws ProcessingException {
        StaxUtil.writeStartElement(writer, NS_PREFIX, "RequestedAttributes", NS_URI);
        StaxUtil.writeNameSpace(writer, NS_PREFIX, NS_URI);

        for (String requestedCitizenAttribute : eAuthIdentityProviderConfig.getRequestedCitizenAttributes()) {

            StaxUtil.writeStartElement(writer, NS_PREFIX, "RequestedAttribute", "");
            StaxUtil.writeAttribute(writer, "FriendlyName", eAuthIdentityProviderConfig.getAttributeFriendlyName(requestedCitizenAttribute));
            StaxUtil.writeAttribute(writer, "Name", requestedCitizenAttribute);
            StaxUtil.writeAttribute(writer, "NameFormat", "urn:oasis:names:tc:saml2:2.0:attrname-format:uri");
            StaxUtil.writeAttribute(writer, "isRequired", "true");
            StaxUtil.writeStartElement(writer, NS_PREFIX, "AttributeValue", "");
            StaxUtil.writeCharacters(writer, requestedCitizenAttribute);
            StaxUtil.writeEndElement(writer);
            StaxUtil.writeEndElement(writer);
        }

        StaxUtil.writeEndElement(writer);

        StaxUtil.flush(writer);

    }

}
