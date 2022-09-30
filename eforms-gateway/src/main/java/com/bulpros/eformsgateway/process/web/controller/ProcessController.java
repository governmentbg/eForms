package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.exception.CheckEDeliveryRegistrationException;
import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.eformsintegrations.service.EDeliveryRegistrationService;
import com.bulpros.eformsgateway.form.exception.NotSatisfiedAssuranceLevel;
import com.bulpros.eformsgateway.form.service.ServiceService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.service.ValidationServiceService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.BusinessKeyService;
import com.bulpros.eformsgateway.process.service.BusinessKeyTypeEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.process.web.dto.*;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import com.bulpros.formio.dto.ResourceDto;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProcessController extends AbstractProcessController {

    private final UserService userService;
    private final ServiceService serviceService;
    private final EDeliveryRegistrationService eDeliveryRegistrationService;
    private final TaskService taskService;
    private final ValidationServiceService validationServiceService;

    public ProcessController(UserService userService, BusinessKeyService businessKeyService,
                             ProcessService processService,
                             UserProfileService userProfileService,
                             ServiceService serviceService, EDeliveryRegistrationService eDeliveryRegistrationService, TaskService taskService, ValidationServiceService validationServiceService) {
        super(userProfileService, businessKeyService, processService);
        this.userService = userService;
        this.serviceService = serviceService;
        this.eDeliveryRegistrationService = eDeliveryRegistrationService;
        this.taskService = taskService;
        this.validationServiceService = validationServiceService;
    }

    @Timed(value = "eforms-gateway-start-process.time")
    @PostMapping("projects/{projectId}/start-process/{arId}")
    public ResponseEntity<StartProcessInstanceResponseDto> startProcess(Authentication authentication,
                                                                        @PathVariable("projectId") String projectId,
                                                                        @PathVariable("arId") String arId,
                                                                        @RequestBody StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                                        @RequestParam(value = "applicant", required = false) String applicant) {

        var userProfile = getUserProfile(authentication, startProcessInstanceRequestDto);

        if (!checkAuthorization(userProfile, applicant, AdditionalProfileRoleEnum.User)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var serviceDto = serviceService.getServiceById(projectId, authentication, arId);
        validationServiceService.validateRequiredProfile(userProfile, serviceDto, applicant);

        User user = userService.getUser(authentication);
        CheckEDeliveryRegistrationResult checkEDeliveryRegistrationResult = eDeliveryRegistrationService.checkRegistration(user,
                authentication, projectId, applicant);

        if (checkEDeliveryRegistrationResult.getStatus() != EDeliveryStatusEnum.OK) {
            throw new CheckEDeliveryRegistrationException(checkEDeliveryRegistrationResult.getStatus().getValue());
        }

        startProcessInstanceRequestDto.setBusinessKeyType(BusinessKeyTypeEnum.ORN);
        String processKey = null;
        try{
            processKey = getServiceProcessKey(projectId, authentication, serviceDto);
        }catch (NotSatisfiedAssuranceLevel assuranceLevelException) {
            throw assuranceLevelException;
        }
        return startProcess(authentication, processKey, startProcessInstanceRequestDto, applicant, false,
                userProfile, user,
                checkEDeliveryRegistrationResult.getProfile(), PUBLIC_CACHE);

    }

    private String getServiceProcessKey(String projectId, Authentication authentication, ResourceDto serviceDto) {
        return serviceService.getProcessKeyByServicesId(projectId, authentication, serviceDto);
    }

    @Timed(value = "eforms-gateway-terminate-process.time")
    @PostMapping(path = "projects/{projectId}/terminate-process/{business-key}")
    public ResponseEntity<TerminateProcessResponseDto> terminateProcess(Authentication authentication,
                                                                        @PathVariable("projectId") String projectId,
                                                                        @PathVariable("business-key") String businessKey,
                                                                        @RequestBody TerminateProcessRequest request,
                                                                        @RequestParam(value = "applicant", required = false) String applicant) {

        var process = processService.getProcessInstanceByBusinessKey(authentication, businessKey, PUBLIC_CACHE);
        var processInstanceId = process.getId();
        var caseData = processService.getProcessVariableAsJsonObject(authentication, processInstanceId, "case");
        if (caseData == null || caseData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String processRequestor = JsonPath.using(pathConfiguration).parse(caseData).read("$.value.data.requestor");
        String processApplicant = JsonPath.using(pathConfiguration).parse(caseData).read("$.value.data.applicant");

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);

        if (applicant != null && !applicant.isEmpty()) {
            if (this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.User) ||
                    this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.Admin)) {
                if (!applicant.equals(processApplicant)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            String personIdentifier = userProfile.getPersonIdentifier();
            if (!personIdentifier.equals(processRequestor)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }

        return ResponseEntity.ok(processService.terminateProcess(authentication, new TerminateProcessRequestDto(businessKey, request.getMessage())));
    }

    @Timed(value = "eforms-gateway-delete-local-variables.time")
    @DeleteMapping(path = "projects/{projectId}/task/{task-id}/local-variables")
    public ResponseEntity<Void> deleteLocalVariables(Authentication authentication,
                                                     @PathVariable("projectId") String projectId,
                                                     @PathVariable("task-id") String taskId) {

        var task = taskService.getTaskById(authentication, taskId);
        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        String personIdentifier = userProfile.getPersonIdentifier();
        if (!task.getAssignee().equals(personIdentifier)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        var localVariables = processService.getLocalVariablesAsJsonArray(authentication, taskId);
        localVariables.keySet().stream().forEach(var -> processService.deleteLocalVariable(authentication, taskId, var));
        return ResponseEntity.ok().build();
    }

    protected boolean checkAuthorization(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum additionalProfileRoleEnum) {
        if (ObjectUtils.isEmpty(applicant)) {
            return true;
        } else {
            return this.userProfileService.hasUserRole(userProfile, applicant, additionalProfileRoleEnum);
        }
    }
}