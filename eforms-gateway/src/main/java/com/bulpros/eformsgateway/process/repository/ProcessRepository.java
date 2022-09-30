package com.bulpros.eformsgateway.process.repository;

import java.util.List;
import java.util.Map;

import com.bulpros.eformsgateway.process.web.dto.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public interface ProcessRepository {

    StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables);
    StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables, String cacheControl);
    TerminateProcessResponseDto terminateProcess(String authorizationToken, TerminateProcessRequestDto terminateProcessRequest);
    StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey);
    StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey, String cacheControl);
    StartProcessInstanceResponseDto getHistoryProcessInstanceByBusinessKey(String authorizationToken, String businessKey);
    StartProcessInstanceResponseDto getHistoryProcessInstanceByBusinessKey(String authorizationToken, String businessKey, String cacheControl);
    String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance);
    String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance, String cacheControl);
    JSONObject getLocalVariablesAsJsonArray(String authorizationToken, String taskId);
    public void deleteLocalVariable(String authorizationToken, String taskId, String name);
    JSONObject getProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name);
    JSONObject getHistoryProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name);
    AdminCaseMessageResponseDto message(String authorizationToken, AdminCaseMessageRequestDto adminCaseMessageRequestDto);
}
