package com.bulpros.eformsgateway.security;

import static com.bulpros.eformsgateway.process.repository.camunda.TaskRepositoryImpl.GET_ALL_HISTORY_TASKS_BY_PROCESS_INSTANCE_ID_CACHE;
import static com.bulpros.eformsgateway.process.repository.camunda.TaskRepositoryImpl.GET_ALL_TASKS_BY_ASSIGNEE_CACHE;
import static com.bulpros.eformsgateway.process.repository.camunda.TaskRepositoryImpl.GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE;
import static com.bulpros.eformsgateway.process.repository.camunda.TaskRepositoryImpl.GET_TASK_BY_ID_CACHE;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CompleteTaskRequestGatewayFilterFactory extends AbstractCompleteRequestGatewayFilterFactory<Object> {

    private final UserProfileService userProfileService;
    private final TaskService taskService;
    private final ProcessService processService;
    private final CacheService cacheService;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) ->
            exchange.getPrincipal().flatMap(principal -> {
                Authentication authentication = (Authentication) principal;
                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
                String taskId = uriVariables.get("taskId");
                TaskResponseDto task;
                try {
                    task = taskService.getTaskById(authentication, taskId);
                } catch (HttpClientErrorException e) {
                    return completeRequest(exchange, e.getStatusCode());
                }
                String processInstanceId = task.getProcessInstanceId();
                JSONObject context = processService.getProcessVariableAsJsonObject(authentication, processInstanceId, "context");
                if (context == null || context.isEmpty()) {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }

                Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
                String projectId = JsonPath.using(pathConfiguration)
                        .parse(context)
                        .read("$.value.formioBaseProject");
                UserProfileDto userProfile = this.userProfileService.getUserProfileData(projectId, authentication);
                String personIdentifier = userProfile.getPersonIdentifier();
                if (!task.getAssignee().equals(personIdentifier)) {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
                
                // Invalidate caches
                boolean getTaskByIdEvicted = cacheService.evictIfPresent(GET_TASK_BY_ID_CACHE, taskId);
                boolean getAllTasksByAssigneeEvicted = cacheService.evictIfPresent(GET_ALL_TASKS_BY_ASSIGNEE_CACHE,
                        personIdentifier);
                boolean getAllTasksByProcessInstanceIdEvicted = cacheService
                        .evictIfPresent(GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, processInstanceId);
                boolean getAllHistoryTasksByProcessInstanceIdEvicted = cacheService
                        .evictIfPresent(GET_ALL_HISTORY_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, processInstanceId);

                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    var responseStatus = exchange.getResponse().getStatusCode();
                    // On successful response (Camunda API)
                    if (responseStatus == HttpStatus.OK || responseStatus == HttpStatus.NO_CONTENT) {
                        CompletableFuture.runAsync(() -> {
                            // Update caches
                            if (getTaskByIdEvicted) {
                                taskService.getTaskById(authentication, taskId);
                            }
                            if (getAllTasksByAssigneeEvicted) {
                                taskService.getProcessInstanceIdsForAllTasksByAssignee(authentication);
                            }
                            if (getAllTasksByProcessInstanceIdEvicted) {
                                taskService.getAllTasksByProcessInstanceId(authentication, processInstanceId);
                            }
                            if (getAllHistoryTasksByProcessInstanceIdEvicted) {
                                taskService.getAllHistoryTasksByProcessInstanceId(authentication, processInstanceId);
                            }
                        });
                    } else if (responseStatus == HttpStatus.NOT_FOUND) {
                        completeRequest(exchange, HttpStatus.OK);
                    }
                }));
            });
    }
}
