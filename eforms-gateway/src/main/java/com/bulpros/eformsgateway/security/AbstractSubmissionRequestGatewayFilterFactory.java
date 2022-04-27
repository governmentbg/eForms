package com.bulpros.eformsgateway.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class AbstractSubmissionRequestGatewayFilterFactory<C> extends AbstractCompleteRequestGatewayFilterFactory<C> {
    
    protected List<String> filterExcludedParams = List.of("page", "size");
    
    protected Mono<Void> loadSubmissions(
            ServerWebExchange exchange, ServerHttpRequest request, Authentication authentication, 
            SubmissionService submissionService, ObjectMapper objectMapper, List<SubmissionFilter> filters) {
        Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
        
        String fullResourcePath = uriVariables.get("resourcePath");
        String projectId = uriVariables.get("projectId");
        
        String response = null;
        Object responseObject = null;
        String resourcePath = null;
        String submissionId = null;
        ResourceDto submission = null;
        Matcher matcher = Pattern.compile("([-/a-zA-Z0-9]+)(/submission/)([0-9a-z]+)").matcher(fullResourcePath);
        if (matcher.find()) {
            resourcePath = matcher.group(1);
            submissionId = matcher.group(3);
            
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap("_id", submissionId), false));
            
            try {
                submission = submissionService.existsWithFilter(new ResourcePath(projectId, resourcePath), authentication, filters);
                submissionId = submission.get_id();
                
                if (submissionId == null || submissionId.isEmpty()) {
                    return completeRequest(exchange, HttpStatus.NOT_FOUND);
                }
                submission = submissionService.getSubmissionById(new ResourcePath(projectId, resourcePath), authentication, submissionId);
            } catch (HttpClientErrorException e) {
                return completeRequest(exchange, e.getStatusCode());
            }
            
            responseObject = submission;
            
        } else {
            resourcePath = fullResourcePath.substring(0, fullResourcePath.indexOf("/submission"));
            try {
                if (request.getQueryParams().getFirst("size") != null && request.getQueryParams().getFirst("page") != null) {
                    String sort = request.getQueryParams().getFirst("sort");
                    Long size = Long.parseLong(request.getQueryParams().getFirst("size"));
                    Long page = Long.parseLong(request.getQueryParams().getFirst("page"));
                    responseObject = 
                            submissionService.getSubmissionsWithFilter(
                                    new ResourcePath(projectId, resourcePath), authentication, filters, page, size, sort);
                } else {
                    responseObject = 
                            submissionService.getSubmissionsWithFilter(
                                    new ResourcePath(projectId, resourcePath), authentication, filters);
                }
            } catch (HttpClientErrorException e) {
                return completeRequest(exchange, e.getStatusCode());
            }
        }
        
        try {
            response = objectMapper.writeValueAsString(responseObject);
        } catch (JsonProcessingException e) {
            log.error("Building a response has failed: " + e.getMessage());
        }
        
        return completeRequest(exchange, HttpStatus.OK, response);
    }
    
    protected List<SubmissionFilter> getFilters(Map<String, String> queryParams) {
        return this.getFilters(queryParams, false);
    }

    protected List<SubmissionFilter> getFilters(Map<String, String> queryParams, boolean isSignee) {
        List<SubmissionFilter> filters = new ArrayList<>();

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (!filterExcludedParams.contains(entry.getKey())) {
                filters.add(SubmissionFilter.build(entry.getKey(), entry.getValue()));
            }
        }

        return filters;
    }
}
