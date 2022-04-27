package com.bulpros.eformsgateway.security;

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
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.service.TaskService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClaimTaskRequestGatewayFilterFactory extends AbstractCompleteRequestGatewayFilterFactory<Object> {
    private final UserProfileService userProfileService;
    private final TaskService taskService;
    private final ProcessService processService;
    private final CacheService cacheService;
    
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            return exchange.getPrincipal().flatMap(principal -> {
                Authentication authentication = (Authentication) principal;
                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
                String taskId = uriVariables.get("taskId");
                var applicant = request.getQueryParams().getFirst("applicant");
                if (applicant == null || applicant.isEmpty()) {
                    return completeRequest(exchange, HttpStatus.BAD_REQUEST);
                }
                
                var task = taskService.getTaskById(authentication, taskId);
                var processInstanceId = task.getProcessInstanceId();
                var context = processService.getProcessVariableAsJsonObject(authentication, processInstanceId, "context");
                if (context == null || context.isEmpty()) {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
                Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
                String projectId = JsonPath.using(pathConfiguration).parse(context).read("$.value.formioBaseProject");
                String supplierEik = JsonPath.using(pathConfiguration).parse(context).read("$.value.serviceSupplier.data.eik");
                
                var userProfile = this.userProfileService.getUserProfileData(projectId, authentication);
                
                if (this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.ServiceManager)) {
                    if (!applicant.equals(supplierEik)) {
                        return completeRequest(exchange, HttpStatus.FORBIDDEN);
                    }
                } else {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
                
                // Invalidate caches
                boolean getTaskByIdEvicted = cacheService.evictIfPresent(GET_TASK_BY_ID_CACHE, taskId);
                boolean getAllTasksByAssigneeEvicted = cacheService.evictIfPresent(GET_ALL_TASKS_BY_ASSIGNEE_CACHE,
                        userProfile.getPersonIdentifier());
                boolean getAllTasksByProcessInstanceIdEvicted = cacheService
                        .evictIfPresent(GET_ALL_TASKS_BY_PROCESS_INSTANCE_ID_CACHE, processInstanceId);
                
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    var responseStatus = exchange.getResponse().getStatusCode();
                    // On successful response (Camunda API)
                    if (responseStatus == HttpStatus.NO_CONTENT) {
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
                        });
                    }
                }));
            });
        };
    }
}
