package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.AdminServiceFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceIdAndNameDto;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceSubmissionResponseDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface ServiceService {
    ResourceDto getServiceAssuranceLevel(String projectId, Authentication authentication, String easId);

    ServiceSubmissionResponseDto getServicesById(String projectId, Authentication authentication, String easId);
    
    ServiceSubmissionResponseDto getServicesById(String projectId, Authentication authentication, String easId, String cacheControl);

    String getProcessKeyByServicesId(String projectId, Authentication authentication, String easId);

    Page<ResourceDto> getServices(String projectId, Authentication authentication, AdminServiceFilter serviceFilter,
                                  Long page, Long size, String sort);

    List<ResourceDto> getServiceSuppliersByTitle(String projectId, String easId, String title, Authentication authentication);

    List<ResourceDto> getServiceSuppliersByTitle(String projectId, String easId, String title, Authentication authentication, String cacheControl);

    List<ServiceIdAndNameDto> getServicesByCaseStatusClassifierAndName(String projectId, Authentication authentication,
                                                                       AdminServiceFilter serviceFilter, String classifier);
}
