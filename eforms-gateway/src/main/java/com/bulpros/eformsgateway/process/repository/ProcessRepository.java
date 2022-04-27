package com.bulpros.eformsgateway.process.repository;

import java.util.Map;

import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessRequestDto;
import com.bulpros.eformsgateway.process.web.dto.TerminateProcessResponseDto;
import net.minidev.json.JSONObject;

public interface ProcessRepository {

    StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables);
    StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables, String cacheControl);
    TerminateProcessResponseDto terminateProcess(String authorizationToken, TerminateProcessRequestDto terminateProcessRequest);
    StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey);
    StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey, String cacheControl);
    String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance);
    String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance, String cacheControl);
    JSONObject getProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name);
    JSONObject getHistoryProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name);
}
