package com.bulpros.eformsgateway.form.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.files.service.FilePermissionEvaluator;
import com.bulpros.eformsgateway.files.service.FileService;
import com.bulpros.eformsgateway.form.service.AdditionalProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.micrometer.core.annotation.Timed;
import lombok.SneakyThrows;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.bulpros.eformsgateway.form.service.CaseService;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminCaseController {

    private final CaseService caseService;
    private final TaskService taskService;
    private final ProcessService processService;
    private final ServiceSupplierService serviceSupplierService;
    private final UserProfileService userProfileService;
    private final AdditionalProfileService additionalProfileService;
    private final FileService fileService;
    private final FilePermissionEvaluator filePermissionEvaluator;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    private final String SIGN_FORM_SUBMISSION_VAR = "submissionData_common_component_attachment_sign";

    public AdminCaseController(CaseService caseService,
                               TaskService taskService, ProcessService processService, ServiceSupplierService serviceSupplierService,
                               UserProfileService userProfileService,
                               AdditionalProfileService additionalProfileService, FileService fileService, @Qualifier("admin") FilePermissionEvaluator filePermissionEvaluator, ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.caseService = caseService;
        this.taskService = taskService;
        this.processService = processService;
        this.serviceSupplierService = serviceSupplierService;
        this.userProfileService = userProfileService;
        this.additionalProfileService = additionalProfileService;
        this.fileService = fileService;
        this.filePermissionEvaluator = filePermissionEvaluator;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @Timed(value = "eforms-gateway-admin-get-case-submissions-by-case-status.time")
    @GetMapping(path = "/projects/{projectId}/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getCaseSubmissionsByCaseStatusClassifier(
            Authentication authentication,
            @PathVariable String projectId,
            @Valid AdminCaseFilter caseFilter,
            @RequestParam(required = false) Long page,
            @RequestParam(required = false) Long size,
            @RequestParam(required = false) String sort) {

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);

        if (userProfileService.hasUserRole(
                userProfile, caseFilter.getApplicant(), AdditionalProfileRoleEnum.ServiceManager)) {
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

        var response = caseService.getAdminCasesByFilterParameters(projectId, authentication,
                caseFilter, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @Timed(value = "eforms-gateway-admin-get-case-submissions-by-id.time")
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

    @SneakyThrows(JsonProcessingException.class)
    @GetMapping(path="/projects/{projectId}/cases/{businessKey}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CaseDetailsDto> handleCaseDetails(Authentication authentication,
                                                            @PathVariable String projectId,
                                                            @PathVariable String businessKey,
                                                            @RequestParam (required = false) String applicant) {

        if (StringUtils.isEmpty(applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String supplier = caseService.getCaseSupplierByBusinessKey(projectId, authentication, businessKey);
        if(supplier == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if(!supplier.equals(applicant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        CaseDetailsDto caseDetailsDto = new CaseDetailsDto();
        if (userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.ServiceManager)) {
            JSONObject signForm = null;
            try {
                signForm = processService.getProcessVariableByBusinessKey(authentication, businessKey,
                        SIGN_FORM_SUBMISSION_VAR);
            } catch (ResourceNotFoundException exception) {
                signForm = processService.getHistoryProcessVariableByBusinessKey(authentication, businessKey,
                        SIGN_FORM_SUBMISSION_VAR);
            }

            if(signForm != null) {
                String startProcessRequestJson = objectMapper.writeValueAsString(signForm);
                Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
                var context = JsonPath.using(pathConfiguration).parse(startProcessRequestJson);
                String requiredSignaturesSelect =  context.read("$.value.data.requiredSignaturesSelect");
                caseDetailsDto.setRequiredSignaturesSelect(requiredSignaturesSelect);
                JSONArray signeesProfiles = context.read("$.value.data.signeesProfiles");
                if(signeesProfiles!=null){
                    List<UserProfileDto> signess =
                            signeesProfiles
                                    .stream()
                                    .map(profile -> modelMapper.map(profile, UserProfileDto.class))
                                    .collect(Collectors.toList());
                    caseDetailsDto.setSigneesProfiles(signess);
                }
            }

            var caseApplicant = caseService.getCaseApplicantByBusinessKey(projectId, authentication, businessKey);
            ResourceDto profileDto = null;
            if(StringUtils.isNotEmpty(caseApplicant)) {
                profileDto = additionalProfileService.getAdditionalProfileNameByApplicant(projectId, authentication, caseApplicant);
                caseDetailsDto.setApplicant(profileDto);
            }
            return ResponseEntity.ok(caseDetailsDto);
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
