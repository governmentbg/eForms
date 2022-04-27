package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.exception.CheckEDeliveryRegistrationException;
import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.eformsintegrations.service.EDeliveryRegistrationService;
import com.bulpros.eformsgateway.form.exception.NotSatisfiedAssuranceLevel;
import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.ServiceService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.BusinessKeyService;
import com.bulpros.eformsgateway.process.service.BusinessKeyTypeEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessRequestDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    public ProcessController(UserService userService, BusinessKeyService businessKeyService,
                             ProcessService processService,
                             UserProfileService userProfileService,
                             ConfigurationProperties configurationProperties,
                             ServiceService serviceService, EDeliveryRegistrationService eDeliveryRegistrationService) {
        super(userProfileService, businessKeyService, processService, configurationProperties);
        this.userService = userService;
        this.serviceService = serviceService;
        this.eDeliveryRegistrationService = eDeliveryRegistrationService;
    }

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

        User user = userService.getUser(authentication);
        CheckEDeliveryRegistrationResult checkEDeliveryRegistrationResult = eDeliveryRegistrationService.checkRegistration(user,
                authentication, projectId, applicant);

        if (checkEDeliveryRegistrationResult.getStatus() != EDeliveryStatusEnum.OK) {
            throw new CheckEDeliveryRegistrationException(checkEDeliveryRegistrationResult.getStatus().getValue());
        }

        startProcessInstanceRequestDto.setBusinessKeyType(BusinessKeyTypeEnum.ORN);
        String processKey = null;
        try{
            processKey = getServiceProcessKey(projectId, authentication, arId);
        }catch (NotSatisfiedAssuranceLevel assuranceLevelException) {
            throw assuranceLevelException;
        }
        return startProcess(authentication, processKey, startProcessInstanceRequestDto, applicant, false,
                userProfile, user,
                checkEDeliveryRegistrationResult.getProfile(), PUBLIC_CACHE);

    }

    private String getServiceProcessKey(String projectId, Authentication authentication, String arId) {
        return serviceService.getProcessKeyByServicesId(projectId, authentication, arId);
    }

    @GetMapping(path = "/terminate-process/key/{business-key}/message/{message}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TerminateProcessResponseDto> terminateProcess(Authentication authentication,
                                                                        @PathVariable("business-key") String businessKey,
                                                                        @PathVariable("message") String message) {
        return ResponseEntity.ok(processService.terminateProcess(authentication, new TerminateProcessRequestDto(businessKey, message)));
    }

    protected boolean checkAuthorization(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum additionalProfileRoleEnum) {
        if (ObjectUtils.isEmpty(applicant)) {
            return true;
        } else {
            return this.userProfileService.hasUserRole(userProfile, applicant, additionalProfileRoleEnum);
        }
    }
}