package com.bulpros.eformsgateway.form.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.files.service.FilePermissionEvaluator;
import com.bulpros.eformsgateway.files.service.FileService;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.bulpros.eformsgateway.form.service.CaseService;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.utils.ValidationUtils;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.InvalidCaseStatusClassifierException;
import com.bulpros.formio.utils.Page;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminCaseController {

    private final CaseService caseService;
    private final UserService userService;
    private final TaskService taskService;
    private final ServiceSupplierService serviceSupplierService;
    private final UserProfileService userProfileService;
    private final FileService fileService;
    private final FilePermissionEvaluator filePermissionEvaluator;

    public AdminCaseController(CaseService caseService, UserService userService,
                               TaskService taskService, ServiceSupplierService serviceSupplierService,
                               UserProfileService userProfileService,
                               FileService fileService, @Qualifier("admin") FilePermissionEvaluator filePermissionEvaluator) {
        this.caseService = caseService;
        this.userService = userService;
        this.taskService = taskService;
        this.serviceSupplierService = serviceSupplierService;
        this.userProfileService = userProfileService;
        this.fileService = fileService;
        this.filePermissionEvaluator = filePermissionEvaluator;
    }

    @GetMapping(path = "/projects/{projectId}/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getCaseSubmissionsByCaseStatusClassifier(
            Authentication authentication,
            @PathVariable String projectId,
            @RequestParam String classifier,
            @Valid AdminCaseFilter caseFilter,
            @RequestParam(required = false) Long page,
            @RequestParam(required = false) Long size,
            @RequestParam(required = false) String sort) {

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);

        if (ValidationUtils.getValidCaseClassifiers(classifier).isEmpty()) {
            log.error("Invalid classifier.");
            throw new InvalidCaseStatusClassifierException("Invalid case status classifier: " + classifier + ".");
        }

        if (userProfileService.hasUserRole(
                userProfile, caseFilter.getApplicant(), AdditionalProfileRoleEnum.ServiceManager)) {
            List<String> processInstanceIds = new ArrayList<>();
            if (Boolean.TRUE.equals(caseFilter.getAssignedToMe())) {
                processInstanceIds = taskService.getProcessInstanceIdsForAllTasksByAssignee(authentication);
                if (processInstanceIds == null || processInstanceIds.isEmpty()) {
                    return getEmptyPageResponse();
                }
            }
            caseFilter.setProcessInstanceIds(processInstanceIds);

            List<String> profileIds = new ArrayList<>();
            if (caseFilter.getRequestorName() != null && !caseFilter.getRequestorName().isEmpty()) {
                List<UserProfileDto> profiles = userProfileService.getUserProfilesByName(projectId, authentication, caseFilter.getRequestorName());
                profileIds = profiles.stream().map(f -> f.getPersonIdentifier()).collect(Collectors.toList());
                if (profileIds.isEmpty()) {
                    return getEmptyPageResponse();
                }
            }
            caseFilter.setUserIds(profileIds);

            if (caseFilter.getApplicant() != null && !caseFilter.getApplicant().isEmpty()) {
                ServiceSupplierDto serviceSupplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, caseFilter.getApplicant());
                if (serviceSupplier == null) {
                    return getEmptyPageResponse();
                }
                caseFilter.setServiceSupplierId(serviceSupplier.getCode());
            } else {
                return getEmptyPageResponse();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var response = caseService.getAdminCasesByUserIdAndStatusClassifier(projectId, authentication,
                classifier, caseFilter, page, size, sort);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projects/{projectId}/cases/{caseBusinessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getCaseSubmissionById(
            Authentication authentication, @PathVariable String projectId, @PathVariable String caseBusinessKey,
            @Valid AdminCaseFilter caseFilter) {

        if (caseFilter.getApplicant() == null || caseFilter.getApplicant().isEmpty()) {
            return getForbiddenResponse();
        }

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);

        if (!userProfileService.hasUserRole(userProfile, caseFilter.getApplicant(), AdditionalProfileRoleEnum.ServiceManager)) {
            return getForbiddenResponse();
        }
        if (caseFilter.getApplicant() != null && !caseFilter.getApplicant().isEmpty()) {

            ServiceSupplierDto serviceSupplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, caseFilter.getApplicant());
            if (serviceSupplier == null) {
                return getEmptyResponse();
            }
            caseFilter.setServiceSupplierId(serviceSupplier.getCode());
        }

        var response = caseService.getAdminCaseByUserIdAndCaseBusinessKey(projectId, authentication, caseBusinessKey, caseFilter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}/cases/files")
    @ResponseBody
    public ResponseEntity<List<FileDto>> handleCaseFiles(Authentication authentication,
                                                         @PathVariable String projectId,
                                                         @Valid AdminCaseFilter caseFilter) {

        FileFilter fileFilter = new FileFilter();
        fileFilter.setBusinessKey(caseFilter.getBusinessKey());
        if (filePermissionEvaluator.hasDownloadPermission(authentication, projectId, caseFilter, fileFilter)) {

            return ResponseEntity.ok().body(fileService.caseFiles(projectId, fileFilter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<ResourceDto> getForbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Page<ResourceDto>> getEmptyPageResponse() {
        return ResponseEntity.ok(new Page<ResourceDto>(0L, 0L, Arrays.asList()));
    }

    private ResponseEntity<ResourceDto> getEmptyResponse() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
