package com.bulpros.eformsgateway.process.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminTaskController {
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ProcessService processService;
    
    @Autowired
    private UserProfileService userProfileService;

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

    @GetMapping(path = "/admin/history-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HistoryTaskResponseDto>> getAllHistoryTasksByProcessInstanceId(
            Authentication authentication,
            @RequestParam String processInstanceId,
            @RequestParam String applicant) {
        
        if (!checkAuthorization(authentication, processInstanceId, applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        
        var response = taskService.getAllHistoryTasksByProcessInstanceId(authentication,
                processInstanceId);
        return ResponseEntity.ok(response);
    }
    
    private boolean checkAuthorization(Authentication authentication, String processInstanceId, String applicant) {
        var context = processService.getHistoryProcessVariableAsJsonObject(authentication, processInstanceId, "context");
        if (context == null || context.isEmpty()) {
            return false;
        }
        
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String projectId = JsonPath.using(pathConfiguration).parse(context).read("$.value.formioBaseProject");
        String supplierEik = JsonPath.using(pathConfiguration).parse(context).read("$.value.serviceSupplier.data.eik");
        
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
