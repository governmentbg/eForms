package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.process.repository.utils.ProcessConstants;
import io.micrometer.core.annotation.Timed;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.process.web.dto.HistoryTaskResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminTaskController {
    private final TaskService taskService;
    private final ProcessService processService;
    private final UserProfileService userProfileService;

    @Timed(value = "eforms-gateway-admin-tasks.time")
    @GetMapping(path = "/admin/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<TaskResponseDto>> getAllTasksByProcessInstanceId(
            Authentication authentication,
            @RequestParam String processInstanceId,
            @RequestParam String applicant) {

        if (!checkAuthorization(authentication, processInstanceId, applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        var response = taskService.getAllTasksByProcessInstanceId(authentication, processInstanceId);
        return ResponseEntity.ok(response);
    }

    @Timed(value = "eforms-gateway-admin-history-tasks.time")
    @GetMapping(path = "/admin/history-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HistoryTaskResponseDto>> getAllHistoryTasksByProcessInstanceId(
            Authentication authentication,
            @RequestParam String processInstanceId,
            @RequestParam String applicant) {

        applicant = applicant.strip();
        if (!checkAuthorization(authentication, processInstanceId, applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        var response = taskService.getAdminHistoryTasksByProcessInstanceId(authentication,
                processInstanceId);
        return ResponseEntity.ok(response);
    }
    
    private boolean checkAuthorization(Authentication authentication, String processInstanceId, String applicant) {
        var context = processService.getHistoryProcessVariableAsJsonObject(authentication, processInstanceId, ProcessConstants.CONTEXT);
        if (context == null || context.isEmpty()) {
            return false;
        }

        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        var documentContext = JsonPath.using(pathConfiguration).parse(context);
        String projectId = documentContext.read("$.value.formioBaseProject");
        String supplierEik = documentContext.read("$.value.serviceSupplier.data.eik");
        
        var userProfile = this.userProfileService.getUserProfileData(projectId, authentication);
        
        if (this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.ServiceManager)) {
            if (!applicant.equals(supplierEik)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
