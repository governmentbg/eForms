package com.bulpros.eformsgateway.files.service;

import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.form.service.CaseService;
import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("user")
@Slf4j
@Getter
@RequiredArgsConstructor
public class UserFilePermissionEvaluator implements FilePermissionEvaluator {

    private final UserService userService;
    private final CaseService caseService;
    private final TaskService taskService;
    private final UserProfileService userProfileService;
    private final ConfigurationProperties configuration;

    @Override
    public boolean hasDownloadPermission(Authentication authentication, String projectId, CaseFilter caseFilter,
                                         FileFilter fileFilter) {
        User user = userService.getUser(authentication);
        caseFilter.setRequestor(user.getPersonIdentifier());

        return caseService.existsCaseByUserIdAndCaseBusinessKey(projectId, authentication, fileFilter.getBusinessKey(),
                caseFilter)
                ||
                !getTaskService().
                        getActiveTasksByProcessInstanceBusinessKeyAndAssignee(authentication, fileFilter.getBusinessKey(),
                                user.getPersonIdentifier()).isEmpty();
    }
}
