package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.AdminCaseFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CaseService {

    Page<ResourceDto> getAdminCasesByUserIdAndStatusClassifier(String projectId, Authentication authentication, String classifier,
                                                               AdminCaseFilter caseFilter, Long page, Long size, String sort);

    ResourceDto getAdminCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                       AdminCaseFilter caseFilter);

    boolean existsAdminCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                      AdminCaseFilter caseFilter);

    Page<ResourceDto> getCasesByUserIdAndStatusClassifier(String projectId, Authentication authentication, String classifier,
                                                          CaseFilter caseFilter, Long page, Long size, String sort);

    ResourceDto getCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                  CaseFilter caseFilter);

    boolean existsCaseByUserIdAndCaseBusinessKey(String projectId, Authentication authentication, String caseBusinessKey,
                                                 CaseFilter caseFilter);

    List<ResourceDto> getIncompleteCasesByUserIdAndServiceIds(String projectId, Authentication authentication,
                                                              List<String> serviceIds);

}
