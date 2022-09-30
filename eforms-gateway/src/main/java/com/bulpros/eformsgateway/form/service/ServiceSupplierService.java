package com.bulpros.eformsgateway.form.service;

import com.bulpros.formio.dto.ResourceDto;
import org.springframework.security.core.Authentication;

import com.bulpros.eformsgateway.form.web.controller.dto.ServiceSupplierDto;

public interface ServiceSupplierService {
    ServiceSupplierDto getServiceSupplierByEik(String projectId, Authentication authentication, String eik);
    ServiceSupplierDto getServiceSupplierByEik(String projectId, Authentication authentication, String eik, String cacheControl);
    ResourceDto getSupplierWithAdminUnitsByCode(String projectId, Authentication authentication, String easId, String code);
    ResourceDto getSupplierWithAdminUnitsByCode(String projectId, Authentication authentication, String easId, String code, String cacheControl);
    ResourceDto getServiceSupplierMetadata(String projectId, Authentication authentication, String easId, String code);
    ResourceDto getServiceSupplierMetadata(String projectId, Authentication authentication, String easId, String code, String cacheControl);
    ResourceDto getServiceSupplierWithChannelTermsAndTaxes(String projectId, Authentication authentication, String easId, String code);
    ResourceDto getServiceSupplierWithChannelTermsAndTaxes(String projectId, Authentication authentication, String easId, String code, String cacheControl);
}
