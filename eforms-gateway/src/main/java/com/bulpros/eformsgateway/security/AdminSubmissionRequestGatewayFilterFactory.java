package com.bulpros.eformsgateway.security;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AdminSubmissionRequestGatewayFilterFactory extends AbstractSubmissionRequestGatewayFilterFactory<Object> {
    
    private final SubmissionService submissionService;
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;
    
    public AdminSubmissionRequestGatewayFilterFactory(
            SubmissionService submissionService,
            UserProfileService userProfileService,
            ObjectMapper objectMapper) {
        this.submissionService = submissionService;
        this.userProfileService = userProfileService;
        this.objectMapper = objectMapper;
    }
    
    
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            return exchange.getPrincipal().flatMap(principal -> {
                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
                
                String fullResourcePath = uriVariables.get("resourcePath");
                if (!fullResourcePath.contains("/submission")) {
                    return chain.filter(exchange);
                }
                
                String projectId = uriVariables.get("projectId");
                String supplier = request.getQueryParams().getFirst("data.supplier");
                
                Authentication authentication = (Authentication) principal;
                UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
                
                if (supplier == null || supplier.isEmpty() || 
                    !userProfileService.hasUserRole(userProfile, supplier, AdditionalProfileRoleEnum.ServiceManager)) {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
                
                List<SubmissionFilter> filters = getFilters(request.getQueryParams().toSingleValueMap());
                
                return loadSubmissions(exchange, request, authentication, submissionService, objectMapper, filters);
                
            });
        };
    }
}
