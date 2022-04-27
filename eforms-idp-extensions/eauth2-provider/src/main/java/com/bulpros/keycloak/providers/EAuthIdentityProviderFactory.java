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

import org.keycloak.Config.Scope;
import org.keycloak.broker.saml.SAMLIdentityProviderFactory;
import org.keycloak.dom.saml.v2.assertion.AttributeType;
import org.keycloak.dom.saml.v2.metadata.*;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;
import org.keycloak.saml.common.exceptions.ParsingException;
import org.keycloak.saml.common.util.DocumentUtil;
import org.keycloak.saml.processing.core.parsers.saml.SAMLParser;
import org.keycloak.saml.validators.DestinationValidator;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EAuthIdentityProviderFactory extends SAMLIdentityProviderFactory {

    public static final String PROVIDER_ID = "eauth2";
    private static final String PROVIDER_NAME = "eAuthentication v2.0";
    public static final String CITIZEN_PROPERTY_NAME = "http://eauth.egov.bg/attributes/citizen";
    public static final String ASSURANCE_CERTIFICATION_PROPERTY_NAME = "urn:oasis:names:tc:SAML:attribute:assurance-certification";
    private static final String MACEDIR_ENTITY_CATEGORY = "http://macedir.org/entity-category";
    private static final String REFEDS_HIDE_FROM_DISCOVERY = "http://refeds.org/category/hide-from-discovery";

    public static final String[] COMPATIBLE_PROVIDERS = new String[] { PROVIDER_ID };

    private DestinationValidator destinationValidator;

    public String getName() {
        return PROVIDER_NAME;
    }

    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void init(Scope config) {
        super.init(config);

        this.destinationValidator = DestinationValidator.forProtocolMap(EAuthIdentityProviderConfig.PROTOCOL_TO_PORT_MAP);
    }

    @Override
    public EAuthIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new EAuthIdentityProvider(session, new EAuthIdentityProviderConfig(model), destinationValidator);
    }

    @Override
    public EAuthIdentityProviderConfig createConfig() {
        return new EAuthIdentityProviderConfig();
    }

    @Override
    public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
        try {
            Object parsedObject = SAMLParser.getInstance().parse(inputStream);
            EntityDescriptorType entityType;

            if (EntitiesDescriptorType.class.isInstance(parsedObject)) {
                entityType = (EntityDescriptorType) ((EntitiesDescriptorType) parsedObject).getEntityDescriptor()
                        .get(0);
            } else {
                entityType = (EntityDescriptorType) parsedObject;
            }

            List<EntityDescriptorType.EDTChoiceType> choiceType = entityType.getChoiceType();

            if (!choiceType.isEmpty()) {
                IDPSSODescriptorType idpDescriptor = null;

                // Metadata documents can contain multiple Descriptors (See ADFS metadata
                // documents) such as RoleDescriptor, SPSSODescriptor, IDPSSODescriptor.
                // So we need to loop through to find the IDPSSODescriptor.
                for (EntityDescriptorType.EDTChoiceType edtChoiceType : entityType.getChoiceType()) {
                    List<EntityDescriptorType.EDTDescriptorChoiceType> descriptors = edtChoiceType.getDescriptors();

                    if (!descriptors.isEmpty() && descriptors.get(0).getIdpDescriptor() != null) {
                        idpDescriptor = descriptors.get(0).getIdpDescriptor();
                    }
                }

                if (idpDescriptor != null) {
                    EAuthIdentityProviderConfig samlIdentityProviderConfig = new EAuthIdentityProviderConfig();
                    String singleSignOnServiceUrl = null;
                    boolean postBindingResponse = false;
                    boolean postBindingLogout = false;
                    for (EndpointType endpoint : idpDescriptor.getSingleSignOnService()) {
                        if (endpoint.getBinding().toString()
                                .equals(JBossSAMLURIConstants.SAML_HTTP_POST_BINDING.get())) {
                            singleSignOnServiceUrl = endpoint.getLocation().toString();
                            postBindingResponse = true;
                            break;
                        } else if (endpoint.getBinding().toString()
                                .equals(JBossSAMLURIConstants.SAML_HTTP_REDIRECT_BINDING.get())) {
                            singleSignOnServiceUrl = endpoint.getLocation().toString();
                        }
                    }
                    String singleLogoutServiceUrl = null;
                    for (EndpointType endpoint : idpDescriptor.getSingleLogoutService()) {
                        if (postBindingResponse && endpoint.getBinding().toString()
                                .equals(JBossSAMLURIConstants.SAML_HTTP_POST_BINDING.get())) {
                            singleLogoutServiceUrl = endpoint.getLocation().toString();
                            postBindingLogout = true;
                            break;
                        } else if (!postBindingResponse && endpoint.getBinding().toString()
                                .equals(JBossSAMLURIConstants.SAML_HTTP_REDIRECT_BINDING.get())) {
                            singleLogoutServiceUrl = endpoint.getLocation().toString();
                            break;
                        }

                    }
                    samlIdentityProviderConfig.setSingleLogoutServiceUrl(singleLogoutServiceUrl);
                    samlIdentityProviderConfig.setSingleSignOnServiceUrl(singleSignOnServiceUrl);
                    samlIdentityProviderConfig.setWantAuthnRequestsSigned(idpDescriptor.isWantAuthnRequestsSigned());
                    samlIdentityProviderConfig.setAddExtensionsElementWithKeyInfo(false);
                    samlIdentityProviderConfig.setValidateSignature(idpDescriptor.isWantAuthnRequestsSigned());
                    samlIdentityProviderConfig.setPostBindingResponse(postBindingResponse);
                    samlIdentityProviderConfig.setPostBindingAuthnRequest(postBindingResponse);
                    samlIdentityProviderConfig.setPostBindingLogout(postBindingLogout);
                    samlIdentityProviderConfig.setLoginHint(false);

                    List<String> nameIdFormatList = idpDescriptor.getNameIDFormat();
                    if (nameIdFormatList != null && !nameIdFormatList.isEmpty())
                        samlIdentityProviderConfig.setNameIDPolicyFormat(nameIdFormatList.get(0));

                    List<KeyDescriptorType> keyDescriptor = idpDescriptor.getKeyDescriptor();
                    String defaultCertificate = null;

                    if (keyDescriptor != null) {
                        for (KeyDescriptorType keyDescriptorType : keyDescriptor) {
                            Element keyInfo = keyDescriptorType.getKeyInfo();
                            Element x509KeyInfo = DocumentUtil.getChildElement(keyInfo,
                                    new QName("dsig", "X509Certificate"));

                            if (KeyTypes.SIGNING.equals(keyDescriptorType.getUse())) {
                                samlIdentityProviderConfig.addSigningCertificate(x509KeyInfo.getTextContent());
                            } else if (KeyTypes.ENCRYPTION.equals(keyDescriptorType.getUse())) {
                                samlIdentityProviderConfig.setEncryptionPublicKey(x509KeyInfo.getTextContent());
                            } else if (keyDescriptorType.getUse() == null) {
                                defaultCertificate = x509KeyInfo.getTextContent();
                            }
                        }
                    }

                    if (defaultCertificate != null) {
                        if (samlIdentityProviderConfig.getSigningCertificates().length == 0) {
                            samlIdentityProviderConfig.addSigningCertificate(defaultCertificate);
                        }

                        if (samlIdentityProviderConfig.getEncryptionPublicKey() == null) {
                            samlIdentityProviderConfig.setEncryptionPublicKey(defaultCertificate);
                        }
                    }

                    samlIdentityProviderConfig
                            .setEnabledFromMetadata(entityType.getValidUntil() == null || entityType.getValidUntil()
                                    .toGregorianCalendar().getTime().after(new Date(System.currentTimeMillis())));

                    // check for hide on login attribute
                    if (entityType.getExtensions() != null
                            && entityType.getExtensions().getEntityAttributes() != null) {
                        for (AttributeType attribute : entityType.getExtensions().getEntityAttributes()
                                .getAttribute()) {
                            if (MACEDIR_ENTITY_CATEGORY.equals(attribute.getName())
                                    && attribute.getAttributeValue().contains(REFEDS_HIDE_FROM_DISCOVERY)) {
                                samlIdentityProviderConfig.setHideOnLogin(true);
                            }
                        }
                        for (AttributeType attribute : entityType.getExtensions().getEntityAttributes()
                                .getAttribute()) {
                            if (CITIZEN_PROPERTY_NAME.equals(attribute.getName())) {
                                if (attribute.getAttributeValue() != null) {
                                    attribute.getAttributeValue()
                                            .forEach(s -> samlIdentityProviderConfig.addCitizenAttribute((String) s));

                                }
                            } else if (ASSURANCE_CERTIFICATION_PROPERTY_NAME.equals(attribute.getName())) {
                                if (attribute.getAttributeValue() != null) {
                                    attribute.getAttributeValue()
                                            .forEach(s -> samlIdentityProviderConfig.addAssuranceLevel((String) s));

                                }
                            }
                        }

                    }

                    return samlIdentityProviderConfig.getConfig();
                }
            }
        } catch (ParsingException pe) {
            throw new RuntimeException("Could not parse IdP SAML Metadata", pe);
        }

        return new HashMap<>();
    }
}
