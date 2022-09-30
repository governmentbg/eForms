package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.web.dto.*;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;

public interface ProcessService {
    StartProcessInstanceResponseDto startProcessInstance(Authentication authentication, String processKey,
                                                         StartProcessInstanceRequestDto startProcessInstanceRequestDto,
                                                         String businessKey, boolean singleInstance,
                                                         UserProfileDto userProfile, String applicant,
                                                         Object eDeliveryProfile, String cacheControl);

    TerminateProcessResponseDto terminateProcess(Authentication authentication, TerminateProcessRequestDto terminateProcessRequest);

    String getBusinessKeyByProcessInstance(Authentication authentication, String processInstance);

    JSONObject getProcessVariableByBusinessKey(Authentication authentication, String businessKey, String name);

    JSONObject getProcessVariableAsJsonObject(Authentication authentication, String processInstance, String name);

    JSONObject getLocalVariablesAsJsonArray(Authentication authentication, String taskId);

    void deleteLocalVariable(Authentication authentication, String taskId, String name);

    JSONObject getHistoryProcessVariableAsJsonObject(Authentication authentication, String processInstance, String name);

    JSONObject getHistoryProcessVariableByBusinessKey(Authentication authentication, String businessKey, String name);

    StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(Authentication authentication, String businessKey, String cacheControl);

    AdminCaseMessageResponseDto message(Authentication authentication, String businessKey, String miscInfo,
                                        AdminCaseMessageRequestDto adminCaseMessageRequestDto);
}
