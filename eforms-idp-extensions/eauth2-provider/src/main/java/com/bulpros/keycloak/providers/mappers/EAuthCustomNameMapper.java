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
package com.bulpros.keycloak.providers.mappers;

import com.bulpros.keycloak.providers.EAuthIdentityProviderFactory;
import org.keycloak.broker.provider.AbstractIdentityProviderMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.saml.SAMLEndpoint;
import org.keycloak.dom.saml.v2.assertion.AssertionType;
import org.keycloak.dom.saml.v2.assertion.AttributeStatementType;
import org.keycloak.dom.saml.v2.assertion.AttributeType;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.IdentityProviderSyncMode;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EAuthCustomNameMapper extends AbstractIdentityProviderMapper {
    private static final String PROVIDER_ID = "euath2-custom-name-mapper";
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();
    public static final String NAME_ATTR = "name-attribute";
    public static final String FR_NAME_ATTR = "friendly-name-attribute";

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName(NAME_ATTR);
        property.setLabel("Name Attribute");
        property.setHelpText("Name of attribute to split into name properties. You can leave this blank and specify a friendly name instead.");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(FR_NAME_ATTR);
        property.setLabel("Name Attribute Friendly Name");
        property.setHelpText("Friendly Name of attribute to split into name properties");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);
    }

    @Override
    public String[] getCompatibleProviders() {
        return EAuthIdentityProviderFactory.COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getDisplayCategory() {
        return "Preprocessor";
    }

    @Override
    public String getDisplayType() {
        return "eAuthentication User Mapper";
    }

    @Override
    public String getHelpText() {
        return "Select Name attribute to split into firstName-lastName properties.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public void preprocessFederatedIdentity(KeycloakSession session, RealmModel realm, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        String attributeName = getAttributeNameFromMapperModel(mapperModel);

        List<String> attributeValuesInContext = findAttributeValuesInContext(attributeName, context);
        if (!attributeValuesInContext.isEmpty()) {
            String fullName = attributeValuesInContext.get(0);
            String[] splitFullName = fullName.split("\\s", 2);

            if (splitFullName.length > 0) {
                context.setFirstName(splitFullName[0]);
            }
            if (splitFullName.length > 1) {
                context.setLastName(splitFullName[1]);
            }
        }
    }

    private List<String> findAttributeValuesInContext(String attributeName, BrokeredIdentityContext context) {
        AssertionType assertion = (AssertionType) context.getContextData().get(SAMLEndpoint.SAML_ASSERTION);

        return assertion.getAttributeStatements().stream()
                .flatMap(statement -> statement.getAttributes().stream())
                .filter(elementWith(attributeName))
                .flatMap(attributeType -> attributeType.getAttribute().getAttributeValue().stream())
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supportsSyncMode(IdentityProviderSyncMode syncMode) {
        boolean syncModeSupported = false;
        switch (syncMode) {
            case FORCE:
            case IMPORT:
                syncModeSupported = true;
        }
        return syncModeSupported;
    }

    private Predicate<AttributeStatementType.ASTChoiceType> elementWith(String attributeName) {
        return attributeType -> {
            AttributeType attribute = attributeType.getAttribute();
            return Objects.equals(attribute.getName(), attributeName)
                    || Objects.equals(attribute.getFriendlyName(), attributeName);
        };
    }

    private String getAttributeNameFromMapperModel(IdentityProviderMapperModel mapperModel) {
        String attributeName = mapperModel.getConfig().get(NAME_ATTR);
        if (attributeName == null) {
            attributeName = mapperModel.getConfig().get(FR_NAME_ATTR);
        }
        return attributeName;
    }
}
