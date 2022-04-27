package com.bulpros.eformsgateway.process.repository.camunda;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.process.repository.TaskRepository;
import com.bulpros.eformsgateway.process.repository.dto.HistoryTaskDto;
import com.bulpros.eformsgateway.process.repository.dto.TaskDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskRepositoryImpl extends BaseRepository implements TaskRepository {
    
    public static final String GET_TASK_BY_ID_CACHE = "getTaskByIdCache";
    public static final String GET_ALL_TASKS_BY_ASSIGNEE_CACHE = "getAllTasksByAssigneeCache";
    public static final String GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE = "getAllTasksByProcessInstanceIdCache";
    public static final String GET_ALL_HISTORY_TASKS_BY_PROCESS_INSTANCE_ID_CACHE = "getAllHistoryTasksByProcessInstanceIdCache";
    
    // Do not removed: Used in @Cacheable condition SpEL expression
    @Getter private final CacheService cacheService;
    
    @Override
    @Cacheable(value = GET_TASK_BY_ID_CACHE, key = "#taskId", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public TaskDto getTaskById(String authorizationToken, String taskId) {
        return getTaskById(authorizationToken, taskId, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_TASK_BY_ID_CACHE, key = "#taskId", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public TaskDto getTaskById(String authorizationToken, String taskId, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);
        
        String url = getTaskUrl(camundaConfProperties.getTaskResourcePath(), null);
        url = url + SLASH_SYMBOL + taskId;
        
        ResponseEntity<TaskDto> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<TaskDto>() {
                });
        return responseEntity.getBody();
    }
    
    @Override
    @Cacheable(value = GET_ALL_TASKS_BY_ASSIGNEE_CACHE, key = "#personIdentifier", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<TaskDto> getAllTasksByAssignee(String authorizationToken, String personIdentifier) {
        return getAllTasksByAssignee(authorizationToken, personIdentifier, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_ALL_TASKS_BY_ASSIGNEE_CACHE, key = "#personIdentifier", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<TaskDto> getAllTasksByAssignee(String authorizationToken, String personIdentifier, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        Map<String, Object> params = Map.of("assignee", personIdentifier);
        String url = getTaskUrl(camundaConfProperties.getTaskResourcePath(), params);

        ResponseEntity<List<TaskDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<TaskDto>>() {
                });
        return responseEntity.getBody();
    }

    @Override
    @Cacheable(value = GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, key = "#processInstanceId", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<TaskDto> getAllTasksByProcessInstanceId(String authorizationToken, String processInstanceId) {
        return getAllTasksByProcessInstanceId(authorizationToken, processInstanceId, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, key = "#processInstanceId", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<TaskDto> getAllTasksByProcessInstanceId(String authorizationToken, String processInstanceId, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        Map<String, Object> params = Map.of("processInstanceId", processInstanceId);
        String url = getTaskUrl(camundaConfProperties.getTaskResourcePath(), params);

        ResponseEntity<List<TaskDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<TaskDto>>() {});
        return responseEntity.getBody();
    }

    @Override
    @Cacheable(value = GET_ALL_HISTORY_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, key = "#processInstanceId", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<HistoryTaskDto> getAllHistoryTasksByProcessInstanceId(String authorizationToken, String processInstanceId) {
        return getAllHistoryTasksByProcessInstanceId(authorizationToken, processInstanceId, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_ALL_HISTORY_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, key = "#processInstanceId", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<HistoryTaskDto> getAllHistoryTasksByProcessInstanceId(String authorizationToken, String processInstanceId, String cacheControl) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        Map<String, Object> params = Map.of("processInstanceId", processInstanceId,
                "sortBy", "startTime", "sortOrder", "asc");
        String url = getTaskUrl(camundaConfProperties.getHistoryTaskResourcePath(), params);

        ResponseEntity<List<HistoryTaskDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<HistoryTaskDto>>() {});
        return responseEntity.getBody();
    }

    @Override
    public List<TaskDto> getActiveTasksByProcessInstanceBusinessKeyAndAssignee(String authorizationToken, String processInstanceBusinessKey,  String assignee) {
        HttpHeaders headers = createHttpAuthorizationHeader(authorizationToken);

        Map<String, Object> params = Map.of("processInstanceBusinessKey", processInstanceBusinessKey,
                                            "assignee", assignee,
                                            "active", true);
        String url = getTaskUrl(camundaConfProperties.getTaskResourcePath(), params);

        ResponseEntity<List<TaskDto>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<TaskDto>>() {});
        return responseEntity.getBody();
    }
}
