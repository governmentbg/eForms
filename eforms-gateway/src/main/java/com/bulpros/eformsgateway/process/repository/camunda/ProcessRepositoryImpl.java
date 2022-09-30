package com.bulpros.eformsgateway.process.repository.camunda;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bulpros.eformsgateway.process.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.process.repository.ProcessRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessRepositoryImpl extends BaseRepository implements ProcessRepository {

    public static final String GET_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE = "getProcessInstanceByBusinessKeyCache";
    public static final String GET_BUSINESS_KEY_BY_PROCESS_INSTANCE_CACHE = "getBusinessKeyByProcessInstanceCache";
    public static final String GET_HISTORY_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE = "getHistoryProcessInstanceByBusinessKey";

    private final ObjectMapper objectMapper;
    @Getter
    private final CacheService cacheService;

    @Override
    public StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables) {
        return startProcessInstanceById(authorizationToken, processId, businessKey, variables, PUBLIC_CACHE);
    }

    @Override
    public StartProcessInstanceResponseDto startProcessInstanceById(String authorizationToken, String processId, String businessKey, Map<String, Object> variables, String cacheControl) {

        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        JsonNode variablesNode = objectMapper.convertValue(variables, JsonNode.class);
        ObjectNode body = objectMapper.createObjectNode();
        body.put("businessKey", businessKey);
        body.set("variables", variablesNode);
        HttpEntity<?> request = new HttpEntity<>(body, headers);

        StartProcessInstanceResponseDto processInstanceResponse = restTemplate.postForEntity(getStartInstanceUrl(processId), request, StartProcessInstanceResponseDto.class).getBody();
        return cacheService.putIfAbsent(GET_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, processInstanceResponse.getBusinessKey(), processInstanceResponse, cacheControl);
    }

    @Override
    public TerminateProcessResponseDto terminateProcess(String authorizationToken, TerminateProcessRequestDto terminateProcessRequest) {

        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<TerminateProcessRequestDto> entity =
                new HttpEntity<>(terminateProcessRequest, headers);
        try {
            ResponseEntity<TerminateProcessResponseDto> response = restTemplate.postForEntity(getTerminateProcessUrl(), entity, TerminateProcessResponseDto.class);
            TerminateProcessResponseDto responseDto = new TerminateProcessResponseDto(response.getStatusCodeValue() == HttpStatus.SC_NO_CONTENT, null);
            if (responseDto.getSuccess()) {
                // Clear caches
                cacheService.evict(GET_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, terminateProcessRequest.getBusinessKey());
            }
            return responseDto;
        } catch (HttpClientErrorException e) {
            return new TerminateProcessResponseDto(false, e.getMessage());
        }
    }

    @Override
    @Cacheable(value = GET_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, key = "#businessKey", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey) {
        return getProcessInstanceByBusinessKey(authorizationToken, businessKey, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, key = "#businessKey", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public StartProcessInstanceResponseDto getProcessInstanceByBusinessKey(String authorizationToken, String businessKey, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        Map<String, String> params = new HashMap<>();
        params.put("businessKey", businessKey);
        try {
            var result = restTemplate.exchange(getProcessInstanceUrl(params), HttpMethod.GET, request, new ParameterizedTypeReference<List<StartProcessInstanceResponseDto>>() {
            })
                    .getBody();
            if (result.isEmpty()) return null;
            return result.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Cacheable(value = GET_HISTORY_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, key = "#businessKey", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public StartProcessInstanceResponseDto getHistoryProcessInstanceByBusinessKey(String authorizationToken, String businessKey) {
        return getHistoryProcessInstanceByBusinessKey(authorizationToken, businessKey, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_HISTORY_PROCESS_INSTANCE_BY_BUSINESS_KEY_CACHE, key = "#businessKey", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public StartProcessInstanceResponseDto getHistoryProcessInstanceByBusinessKey(String authorizationToken, String businessKey, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        Map<String, String> params = new HashMap<>();
        params.put("processInstanceBusinessKey", businessKey);
        try {
            var result = restTemplate.exchange(getHistoryProcessInstanceUrl(params), HttpMethod.GET, request, new ParameterizedTypeReference<List<StartProcessInstanceResponseDto>>() {
            })
                    .getBody();
            if (result.isEmpty()) return null;
            return result.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Cacheable(value = GET_BUSINESS_KEY_BY_PROCESS_INSTANCE_CACHE, key = "#processInstance", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance) {
        return getBusinessKeyByProcessInstance(authorizationToken, processInstance, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_BUSINESS_KEY_BY_PROCESS_INSTANCE_CACHE, key = "#processInstance", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public String getBusinessKeyByProcessInstance(String authorizationToken, String processInstance, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            var result = restTemplate.exchange(
                    getProcessInstanceUrl(new HashMap<>()) + "/" + processInstance, HttpMethod.GET, request, StartProcessInstanceResponseDto.class)
                    .getBody();
            if (result == null) return null;
            return result.getBusinessKey();
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject getLocalVariablesAsJsonArray(String authorizationToken, String taskId) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            var result = restTemplate.exchange(
                    getTaskUrl("task", new HashMap<String, Object>()) + "/" + taskId +
                            "/localVariables", HttpMethod.GET, request, JSONObject.class)
                    .getBody();
            return result;
        } catch (Exception e) {
            log.error("Local variables from task with id: " + taskId + " could not be read!");
            log.error("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteLocalVariable(String authorizationToken, String taskId, String name) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            restTemplate.exchange(
                    getTaskUrl("task", new HashMap<String, Object>()) + "/" + taskId +
                            "/localVariables/" + name, HttpMethod.DELETE, request, JSONObject.class)
                    .getBody();
        } catch (Exception e) {
            log.error("Local variable: " + name + " could not be deleted. Task id: " + taskId + ".");
            log.error("Reason: " + e.getMessage());
        }
    }

    @Override
    public JSONObject getProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            var result = restTemplate.exchange(
                    getProcessInstanceUrl(new HashMap<String, String>()) + "/" + processInstance +
                            "/variables/" + name, HttpMethod.GET, request, JSONObject.class)
                    .getBody();
            if (result == null) return null;
            return result;
        } catch (Exception e) {
            log.error("Process variable: " + name + " could not be read. Process id: " + processInstance + ".");
            log.error("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public JSONObject getHistoryProcessVariableAsJsonObject(String authorizationToken, String processInstance, String name) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);
        var parameters = new HashMap<String, String>();
        parameters.put("variableName", name);
        parameters.put("processInstanceId", processInstance);
        var url = getHistoryVariableProcessesUrl(parameters);
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        try {
            var result = restTemplate.exchange(url, HttpMethod.GET, request, JSONArray.class).getBody();
            if (result.isEmpty()) return null;
            return new JSONObject((Map<String, Object>) result.get(0));
        } catch (Exception e) {
            log.error("Process variable: " + name + " could not be read. Process id: " + processInstance + ".");
            log.error("Reason: " + e.getMessage());
            return null;
        }
    }

    @Override
    public AdminCaseMessageResponseDto message(String authorizationToken, AdminCaseMessageRequestDto adminCaseMessageRequestDto) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        HttpEntity<?> request = new HttpEntity<>(adminCaseMessageRequestDto, headers);

        return restTemplate.postForEntity(getAdminCaseMessageUrl(), request, AdminCaseMessageResponseDto.class).getBody();
    }
}
