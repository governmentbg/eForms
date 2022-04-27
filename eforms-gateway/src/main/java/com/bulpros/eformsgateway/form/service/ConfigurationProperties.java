package com.bulpros.eformsgateway.form.service;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration
@PropertySource("classpath:gateway-resource-${spring.profiles.active}.properties")
public class ConfigurationProperties {

    @Value("${com.bulpros.metadata.services}")
    private String metadataServices;

    @Value("${com.bulpros.formio.userprofile.resource.path}")
    private String userProfilePath;

    @Value("${com.bulpros.formio.additional-userprofile.resource.path}")
    private String additionalUserProfilePath;

    @Value("${com.bulpros.formio.user.id.property.key}")
    private String userIdPropertyKey;

    @Value("${com.bulpros.formio.service.resource.path}")
    private String serviceResourcePath;

    @Value("${com.bulpros.formio.service-suppliers.resource.path}")
    private String serviceSuppliersResourcePath;
    
    @Value("${com.bulpros.formio.service.category.id.property.key}")
    private String serviceCategoryIdPropertyKey;
    
    @Value("${com.bulpros.service.filter.max.size}")
    private Integer serviceFilterMaxSize;

    @Value("${com.bulpros.formio.service.process.security.level.property.key}")
    private String requiredSecurityLevel;

    @Value("${com.bulpros.formio.provider.resource.path}")
    private String providerResourcePath;
    
    @Value("${com.bulpros.formio.eik.property.key}")
    private String eikPropertyKey;

    @Value("${com.bulpros.formio.userprofile.resource.path}")
    private String userprofileResourcePath;

    @Value("${com.bulpros.formio.case.resource.path}")
    private String caseResourcePath;

    @Value("${com.bulpros.formio.case.status.resource.path}")
    private String caseStatusResourcePath;

    @Value("${com.bulpros.formio.case.stage.resource.path}")
    private String caseStageResourcePath;

    @Value("${com.bulpros.formio.case.channel.resource.path}")
    private String caseChannelResourcePath;

    @Value("${com.bulpros.formio.case.status.code.property.key}")
    private String caseStatusCodePropertyKey;

    @Value("${com.bulpros.formio.case.status.classifier.property.key}")
    private String caseStatusClassifierPropertyKey;

    @Value("${com.bulpros.formio.user.name.property.key}")
    private String userNamePropertyKey;

    @Value("${com.bulpros.formio.code.property.key}")
    private String codePropertyKey;

    @Value("${com.bulpros.formio.suppliers.eas.property.key}")
    private String supplierEasPropertyKey;

    @Value("${com.bulpros.formio.service.id.property.key}")
    private String serviceIdPropertyKey;

    @Value("${com.bulpros.formio.service.name.property.key}")
    private String serviceNamePropertyKey;

    @Value("${com.bulpros.formio.service.process.definition.property.key}")
    private String processDefinitionId;

    @Value("${com.bulpros.formio.case.business.key.property.key}")
    private String businessKey;

    @Value("${com.bulpros.formio.case.service.name.property.key}")
    private String serviceName;

    @Value("${com.bulpros.formio.case.requestor.property.key}")
    private String requestorPropertyKey;

    @Value("${com.bulpros.formio.case.applicant.property.key}")
    private String applicantPropertyKey;

    @Value("${com.bulpros.formio.case.supplier.property.key}")
    private String supplierPropertyKey;

    @Value("${com.bulpros.formio.case.serviceSupplierId.property.key}")
    private String serviceSupplierIdPropertyKey;

    @Value("${com.bulpros.formio.case.channel.type.property.key}")
    private String channelTypePropertyKey;

    @Value("${com.bulpros.formio.additional-profile.roles.property.key}")
    private String additionalProfileRolesPropertyKey;

    @Value("${com.bulpros.formio.additional-profile.type.property.key}")
    private String additionalProfileTypePropertyKey;

    @Value("${com.bulpros.formio.additional-profile.person-identifier.property.key}")
    private String additionalProfilePersonIdentifierPropertyKey;

    @Value("${com.bulpros.formio.additional-profile.identifier.property.key}")
    private String additionalProfileIdentifierPropertyKey;
    
    @Value("${com.bulpros.formio.resource.property.pathname.personidentifier}")
    private String personIdentifier;

    @Value("${com.bulpros.formio.supplier.administrative.units.list.property.key}")
    private String administrativeUnitsList;

    @Value("${com.bulpros.formio.supplier.has.administrative.units.property.key}")
    private String hasAdministrativeUnits;

    @Value("${com.bulpros.formio.service.ais.client.epayment.property.key}")
    private String aisClientEPayment;

    @Value("${com.bulpros.formio.service.has.fixed.payment}")
    private String hasFixedPayment;

    @Value("${com.bulpros.formio.service.has.payment}")
    private String hasPayment;

    @Value("${com.bulpros.formio.service.processing.key.property.key}")
    private String processingKey;

    @Value("${com.bulpros.formio.service.description.key.property.key}")
    private String serviceDescription;

    @Value("${com.bulpros.formio.resource.property.pathname.ekattenumber}")
    private String ekatteNumber;

    @Value("${com.bulpros.formio.resource.property.pathname.districtcorrespondence}")
    private String districtCorrespondence;

    @Value("${com.bulpros.formio.resource.property.pathname.citycorrespondence}")
    private String cityCorrespondence;

    @Value("${com.bulpros.formio.resource.property.pathname.municipalitycorrespondence}")
    private String municipalityCorrespondence;

    @Value("${com.bulpros.formio.resource.property.pathname.adrresslinecorrespondence}")
    private String adrresslineCorrespondence;

    @Value("${com.bulpros.formio.resource.property.pathname.phone}")
    private String phone;

    @Value("${com.bulpros.formio.resource.property.pathname.email}")
    private String email;

    @Value("${com.bulpros.formio.resource.property.pathname.isActive}")
    private String isActive;

    @Value("${com.bulpros.formio.resource.property.pathname.emailAuthorised}")
    private String emailAuthorised;

    @Value("${com.bulpros.formio.resource.property.pathname.phoneAuthorised}")
    private String phoneAuthorised;

    @Value("${com.bulpros.formio.resource.property.pathname.ekatteAuthorised}")
    private String ekatteAuthorised;

    @Value("${com.bulpros.formio.resource.property.pathname.adrresslineCorrespondenceAuthorised}")
    private String adrresslineCorrespondenceAuthorised;

    @Value("${com.bulpros.formio.ar.id.property.key}")
    private String arId;

    @Value("${com.bulpros.formio.process.instance.id.property.key}")
    private String processInstanceId;

    @Value("${com.bulpros.formio.issue.date.property.key}")
    private String issueDate;

    @Value("${com.bulpros.formio.status.property.key}")
    private String status;

    @Value("${com.bulpros.formio.additional-userprofile.resource.property.pathname.status}")
    private String additionalUserProfileStatus;

    @Value("${com.bulpros.formio.additional-userprofile.resource.property.pathname.roles}")
    private String additionalUserProfileRoles;

    @Value("${com.bulpros.formio.additional-userprofile.resource.roles.to.sync}")
    private String rolesToSync;

    public boolean isMetadataProcess(String processKey) {
        String[] services = metadataServices.split(",");
        List<String> servicesList = Arrays.asList(services);
        return servicesList.stream()
                .map(String::trim)
                .filter(s-> s.equals(processKey))
                .findFirst().isPresent();
    }
}