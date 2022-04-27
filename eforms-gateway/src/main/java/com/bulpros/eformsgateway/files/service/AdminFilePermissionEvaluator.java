package com.bulpros.eformsgateway.files.service;

import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.form.service.CaseService;
import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.security.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("admin")
@Slf4j
@Getter
@RequiredArgsConstructor
public class AdminFilePermissionEvaluator implements FilePermissionEvaluator {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final CaseService caseService;
    private final TaskService taskService;
    private final ServiceSupplierService serviceSupplierService;
    private final ConfigurationProperties configuration;

    @Override
    public boolean hasDownloadPermission(Authentication authentication, String projectId, CaseFilter caseFilter,
                                         FileFilter fileFilter) {
        AdminCaseFilter adminCaseFilter = (AdminCaseFilter) caseFilter;
        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);

        if (userProfileService.hasUserRole(
                userProfile, adminCaseFilter.getApplicant(), AdditionalProfileRoleEnum.ServiceManager)) {
            ServiceSupplierDto serviceSupplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, adminCaseFilter.getApplicant());
            if (serviceSupplier == null) {
                return false;
            }
            adminCaseFilter.setServiceSupplierId(serviceSupplier.getCode());
        } else {
            return false;
        }

        return caseService.existsAdminCaseByUserIdAndCaseBusinessKey(projectId, authentication, fileFilter.getBusinessKey(),
                adminCaseFilter)
                ||
                !getTaskService().
                        getActiveTasksByProcessInstanceBusinessKeyAndAssignee(authentication, fileFilter.getBusinessKey(),
                                userProfile.getPersonIdentifier()).isEmpty();
    }
}
