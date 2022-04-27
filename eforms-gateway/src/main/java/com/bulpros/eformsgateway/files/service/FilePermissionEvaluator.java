package com.bulpros.eformsgateway.files.service;

import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.eformsgateway.process.repository.utils.EFormsUtils;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface FilePermissionEvaluator {

    UserService getUserService();

    TaskService getTaskService();

    default boolean hasPermission(Authentication authentication, FileFilter fileFilter) {
        User user = getUserService().getUser(authentication);
        List<TaskResponseDto> activeTasks = getTaskService().
                getActiveTasksByProcessInstanceBusinessKeyAndAssignee(authentication, fileFilter.getBusinessKey(), user.getPersonIdentifier());
        if (!activeTasks.isEmpty()) {
            return activeTasks.stream().anyMatch(t -> EFormsUtils.getFormApiPath(t.getFormKey())
                    .equals(StringUtils.isEmpty(fileFilter.getDocumentId()) ? fileFilter.getFormId() : fileFilter.getDocumentId()));
        }
        return false;
    }

    boolean hasDownloadPermission(Authentication authentication, String projectId, CaseFilter caseFilter,
                                  FileFilter fileFilter);

}
