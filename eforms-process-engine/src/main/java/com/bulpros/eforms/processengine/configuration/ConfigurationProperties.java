package com.bulpros.eforms.processengine.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:resource-${spring.profiles.active}.properties")
public class ConfigurationProperties {

    @Value("${com.bulpros.process.projectId.path.expr}")
    private String projectIdPathExpr;

    @Value("${com.bulpros.process.applicant.path.expr}")
    private String applicantPathExpr;

    @Value("${com.bulpros.process.supplier.path.expr}")
    private String supplierPathExpr;

    @Value("${com.bulpros.process.supplier.title.path.expr}")
    private String supplierTitlePathExpr;

    @Value("${com.bulpros.process.supplier.oid.path.expr}")
    private String supplierOIDPathExpr;

    @Value("${com.bulpros.process.arId.path.expr}")
    private String arIdPathExpr;

    @Value("${com.bulpros.process.serviceName.path.expr}")
    private String serviceNamePathExpr;

    @Value("${com.bulpros.process.serviceOID.path.expr}")
    private String serviceOIDPathExpr;

    @Value("${com.bulpros.process.form.attachment.json-path-query}")
    private String formAttachmentJsonPathQuery;

    @Value("${com.bulpros.process.form.filename.json-path-query}")
    private String formFilenameJsonPathQuery;

    @Value("${com.bulpros.process.form.generate-files-package.json-path-query}")
    private String formGenerateFilesPackageJsonPathQuery;

    @Value("${com.bulpros.process.form.is-signable.json-path-query}")
    private String formIsSignableJsonPathQuery;

    @Value("${com.bulpros.process.form.signees.identifiers.json-path-query}")
    private String formSigneesIdentifiersJsonPathQuery;

    @Value("${com.bulpros.process.form.signees.required-signatures.json-path-query}")
    private String formSigneesRequiredSignaturesJsonPathQuery;

    @Value("${com.bulpros.process.form.channel.type.json-path-query}")
    private String formChannelTypeJsonPathQuery;

    @Value("${com.bulpros.process.form.deadline.type.json-path-query}")
    private String deadlineTypeJsonPathQuery;

    @Value("${com.bulpros.process.ePayment.callbackURL}")
    private String ePaymentCallbackURL;

    @Value("${com.bulpros.process.notification.subject}")
    private String notificationSubject;

    @Value("${com.bulpros.process.notification.body}")
    private String notificationBody;

    @Value("${com.bulpros.formio.userprofile.resource.path}")
    private String userProfilePath;

    @Value("${com.bulpros.formio.additional-userprofile.resource.path}")
    private String additionalUserProfilePath;

    @Value("${com.bulpros.formio.user.id.property.key}")
    private String userIdPropertyKey;
    
    @Value("${com.bulpros.formio.user.is-acitve.property.key}")
    private String userIsActivePropertyKey;

    @Value("${com.bulpros.process.eDelivery.json.filename}")
    private String eDeliveryPdfFilename;

    @Value("${com.bulpros.formio.status.property.key}")
    private String statusPropertyKey;

    @Value("${com.bulpros.formio.code.property.key}")
    private String codePropertyKey;

    @Value("${com.bulpros.formio.eik.property.key}")
    private String eikPropertyKey;

    @Value("${com.bulpros.formio.title.property.key}")
    private String titlePropertyKey;

    @Value("${com.bulpros.formio.service.name.property.key}")
    private String serviceNamePropertyKey;

    @Value("${com.bulpros.formio.service.channel.type.property.key}")
    private String serviceChannelTypePropertyKey;

    @Value("${com.bulpros.formio.service.service.type.property.key}")
    private String serviceTypePropertyKey;

    @Value("${com.bulpros.formio.suppliers.eas.property.key}")
    private String serviceSupplierPropertyKey;

    @Value("${com.bulpros.formio.ar.id.property.key}")
    private String arIdPropertyKey;
    
    @Value("${com.bulpros.eforms-public-portal.url}")
    private String publicPortalUrl;
    
    @Value("${com.bulpros.efroms-public-portal.current-task.url}")
    private String publicPortalCurrentTaskUrl;

    @Value("${com.bulpros.formio.suppliers.resourse.name}")
    private String supplierResourceName;

    @Value("${com.bulpros.formio.eservice.resourse.name}")
    private String eServiceResourceName;

    @Value("${com.bulpros.formio.eas-suppliers.resourse.name}")
    private String easSuppliersResourceName;

    @Value("${com.bulpros.formio.term-taxes.resourse.name}")
    private String easTermAndTaxesResourceName;

    @Value("${com.bulpros.allow.deactivate.services}")
    private boolean deactivateServices;

    @Value("${com.bulpros.allow.deactivate.services-suppliers}")
    private boolean deactivateServiceSuppliers;

    @Value("${com.bulpros.eforms-integrations.url}")
    private String integrationsUrl;

    @Value("${com.bulpros.eforms-integrations.egov.prefix}")
    private String egovPrefix;

    public String getProjectIdPathExpr() {
        return "#{" + projectIdPathExpr + "}";
    }

    public String getApplicantPathExpr() {
        return "#{" + applicantPathExpr + "}";
    }

    public String getSupplierPathExpr() {
        return "#{" + supplierPathExpr + "}";
    }

    public String getSupplierTitlePathExpr() {
        return "#{" + supplierTitlePathExpr + "}";
    }

    public String getSupplierOIDPathExpr() {
        return "#{" + supplierOIDPathExpr + "}";
    }

    public String getArIdPathExpr() {
        return "#{" + arIdPathExpr + "}";
    }

    public String getServiceNamePathExpr() {
        return "#{" + serviceNamePathExpr + "}";
    }

    public String getServiceOIDPathExpr() {
        return "#{" + serviceOIDPathExpr + "}";
    }
}
