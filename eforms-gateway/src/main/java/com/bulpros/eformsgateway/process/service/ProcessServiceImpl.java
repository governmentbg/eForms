package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileDto;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.repository.ProcessRepository;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessRequestDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

    private final ProcessRepository processRepository;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    @Override
    public StartProcessInstanceResponseDto startProcessInstance(Authentication authentication, String processKey,
                                                                StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                                String businessKey, boolean singleInstance,
                                                                UserProfileDto userProfile, String applicant,
                                                                Object eDeliveryProfile, String cacheControl) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        Map<String, Object> initiatorVariable = new HashMap<>();
        initiatorVariable.put("value", user.getPersonIdentifier());
        Map<String, Object> variables = startProcessInstanceRequestDto.getVariables();
        variables.put("initiator", initiatorVariable);
        TypeReference<Map<String, Object>> mapType = new TypeReference<>() {};
        Map<String, Object> userProfileMap = objectMapper.convertValue(userProfile, mapType);
        userProfileMap.put("eDeliveryProfile", eDeliveryProfile);
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String variablesJson = new JSONObject(variables).toString();
        if (applicant != null && !applicant.isEmpty()) {
            AdditionalProfileDto additionalProfile = userProfileService.getAdditionalProfileByApplicant(userProfile, applicant);
            userProfileMap.put("applicant", additionalProfile);
        }
        variables = JsonPath
                .using(pathConfiguration)
                .parse(variablesJson)
                .put("$.context.value", "userProfile", userProfileMap)
                .json();
        if (singleInstance) {
            StartProcessInstanceResponseDto processInstanceResponseDto = getProcessInstanceByBusinessKey(token, businessKey, cacheControl);
            return processInstanceResponseDto == null ?
                    processRepository.startProcessInstanceById(token, processKey, businessKey, variables, cacheControl) : processInstanceResponseDto;
        }
        return processRepository.startProcessInstanceById(token, processKey, businessKey, variables, cacheControl);
    }

    @Override
    public TerminateProcessResponseDto terminateProcess(Authentication authentication, TerminateProcessRequestDto terminateProcessRequest) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        return processRepository.terminateProcess(token, terminateProcessRequest);
    }

    @Override
    public String getBusinessKeyByProcessInstance(Authentication authentication, String processInstance) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        return processRepository.getBusinessKeyByProcessInstance(token, processInstance);
    }

    public JSONObject getProcessVariableByBusinessKey(Authentication authentication, String businessKey, String name) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        var processInstance = processRepository.getProcessInstanceByBusinessKey(token, businessKey);
        return processRepository.getProcessVariableAsJsonObject(token, processInstance.getId(), name);
    }

    @Override
    public JSONObject getProcessVariableAsJsonObject(Authentication authentication, String processInstance, String name) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        return processRepository.getProcessVariableAsJsonObject(token, processInstance, name);
    }

    @Override
    public JSONObject getHistoryProcessVariableAsJsonObject(Authentication authentication, String processInstance, String name) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        return processRepository.getHistoryProcessVariableAsJsonObject(token, processInstance, name);
    }

    private StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String token, String businessKey, String cacheControl) {
        return processRepository.getProcessInstanceByBusinessKey(token, businessKey, cacheControl);
    }
}
