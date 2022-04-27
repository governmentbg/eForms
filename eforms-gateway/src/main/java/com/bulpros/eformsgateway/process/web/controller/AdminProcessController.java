package com.bulpros.eformsgateway.process.web.controller;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.BusinessKeyService;
import com.bulpros.eformsgateway.process.service.BusinessKeyTypeEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminProcessController extends AbstractProcessController {

    private final UserService userService;

    public AdminProcessController(UserService userService, BusinessKeyService businessKeyService,
                                  ProcessService processService, UserProfileService userProfileService,
                                  ConfigurationProperties configurationProperties) {
        super(userProfileService, businessKeyService, processService, configurationProperties);
        this.userService = userService;
    }

    @PostMapping("/start-process/{process-key}")
    public ResponseEntity<StartProcessInstanceResponseDto> startProcess(Authentication authentication,
                                                                        @PathVariable("process-key") String processKey,
                                                                        @RequestHeader(CACHE_CONTROL) String cacheControl,
                                                                        @RequestBody StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                                        @RequestParam(value = "applicant", required = false) String applicant) {

        if(!configurationProperties.isMetadataProcess(processKey)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if(applicant==null || ObjectUtils.isEmpty(applicant)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        var userProfile = getUserProfile(authentication, startProcessInstanceRequestDto);
        if (!checkAuthorization(userProfile, applicant, AdditionalProfileRoleEnum.MetadataManager)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        User user = userService.getUser(authentication);
        startProcessInstanceRequestDto.setBusinessKeyType( BusinessKeyTypeEnum.GENERATED);
        startProcessInstanceRequestDto.setApplicant(applicant);
        return startProcess(authentication, processKey, startProcessInstanceRequestDto, applicant, true,
                userProfile, user,
                null, cacheControl);

    }


    protected boolean checkAuthorization(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum additionalProfileRoleEnum) {
        return this.userProfileService.hasUserRole(userProfile, applicant, additionalProfileRoleEnum);
    }
}