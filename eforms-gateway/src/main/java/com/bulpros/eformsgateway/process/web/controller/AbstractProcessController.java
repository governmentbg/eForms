package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.BusinessKeyService;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.user.model.User;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class AbstractProcessController {
    protected final UserProfileService userProfileService;
    private final BusinessKeyService businessKeyService;
    protected final ProcessService processService;
    protected final ConfigurationProperties configurationProperties;

    protected UserProfileDto getUserProfile(Authentication authentication, StartProcessInstanceRequestDto startProcessInstanceRequestDto) {
        String startProcessVariables = new JSONObject(startProcessInstanceRequestDto.getVariables()).toString();
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String projectId = JsonPath.using(pathConfiguration).parse(startProcessVariables).read("$.context.value.formioBaseProject");

        return this.userProfileService.getUserProfileData(projectId, authentication);
    }


    protected ResponseEntity<StartProcessInstanceResponseDto> startProcess(Authentication authentication, String processKey,
                                                                           StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                                           String applicant, boolean singleInstance,
                                                                           UserProfileDto userProfile, User user, Object eDeliveryProfile,
                                                                           String cacheControl) {
        String businessKeyValue = businessKeyService.generateBusinessKey(processKey, startProcessInstanceRequestDto, user);
        if (businessKeyValue == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var process = processService.startProcessInstance(authentication, processKey,
                startProcessInstanceRequestDto, businessKeyValue, singleInstance, userProfile, applicant,
                eDeliveryProfile, cacheControl);
        return ResponseEntity.ok(process);
    }
}
