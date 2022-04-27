package com.bulpros.eformsgateway.security;

import java.util.*;

import com.bulpros.eformsgateway.form.web.controller.dto.IdentifierTypeEnum;
import com.bulpros.eformsgateway.process.repository.utils.EFormsUtils;
import com.bulpros.eformsgateway.process.repository.utils.ProcessConstants;
import com.bulpros.eformsgateway.process.service.ProcessService;
import net.minidev.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.FormsService;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import net.minidev.json.JSONArray;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;

@Component
public class SubmissionRequestGatewayFilterFactory extends AbstractSubmissionRequestGatewayFilterFactory<Object> {
    private final SubmissionService submissionService;
    private final FormsService formService;
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;
    private ConfigurationProperties configuration;
    private final ProcessService processService;

    public SubmissionRequestGatewayFilterFactory(
            SubmissionService submissionService,
            FormsService formService,
            UserProfileService userProfileService,
            ConfigurationProperties configurationProperties,
            ObjectMapper objectMapper, ProcessService processService) {
        this.submissionService = submissionService;
        this.formService = formService;
        this.userProfileService = userProfileService;
        this.configuration = configurationProperties;
        this.objectMapper = objectMapper;
        this.processService = processService;
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
                String businessKey = request.getQueryParams().getFirst("data.businessKey");
                String applicant = request.getQueryParams().getFirst("data.applicant");
                
                Authentication authentication = (Authentication) principal;
                UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
                String user = userProfile.getPersonIdentifier().substring(
                        IdentifierTypeEnum.EGN.getPrefix().length() + 1);

                var isCurrentUserSignee = isUserSigneeForProcess(authentication, exchange, businessKey, user);

                if (applicant != null && !applicant.isEmpty() && !isAllowedApplicant(userProfile, applicant)) {
                        return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
                Map<String, String> queryParams = request.getQueryParams().toSingleValueMap();
                if (applicant == null || applicant.isEmpty()) {
                    String resourcePath = fullResourcePath.substring(0, fullResourcePath.indexOf("/submission"));
                    JsonNode metadataJson = null;
                    try {
                        metadataJson = formService.getFormJson(projectId, resourcePath, authentication);
                    } catch (HttpClientErrorException e) {
                        return completeRequest(exchange, e.getStatusCode());
                    }
                    String metadata = metadataJson.toString();
                    if (jsonExists(metadata, "$..[?(@.key == 'requestor')]")) {
                        queryParams.put("data.requestor", userProfile.getPersonIdentifier());
                    }
                }

                List<SubmissionFilter> filters = getFilters(queryParams, isCurrentUserSignee);
                return loadSubmissions(exchange, request, authentication, submissionService, objectMapper, filters);
            });
        };
    }
    
    @Override
    protected List<SubmissionFilter> getFilters(Map<String, String> queryParams, boolean isSignee) {
        List<SubmissionFilter> filters = super.getFilters(queryParams, isSignee);
        if(isSignee) {
            for (Iterator<SubmissionFilter> iterator = filters.iterator(); iterator.hasNext();) {
                SubmissionFilter filter = iterator.next();
                if(filter.getProperties().containsKey("requestor")){
                    iterator.remove();
                }
            }
            return filters;
        }
        String applicant = queryParams.get("data.applicant");
        String requestor = queryParams.get("data.requestor");
        if ((applicant == null || applicant.isEmpty()) &&
            requestor != null && !requestor.isEmpty()) {
            filters.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.EXISTS,
                    Collections.singletonMap(configuration.getApplicantPropertyKey(), false)));
        }

        return filters;
    }

    private boolean isUserSigneeForProcess(Authentication authentication, ServerWebExchange exchange,
                                           String businessKey, String personIdentifier) {
        if(businessKey==null || businessKey.isEmpty()) return false;
        String formSubmissionData = ProcessConstants.SUBMISSION_DATA + EFormsUtils.getFormDataSubmissionKey("common/component/selectSignees");

        JSONObject signees = processService.getProcessVariableByBusinessKey(authentication, businessKey, formSubmissionData);
        if(signees == null) return false;
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        List<LinkedHashMap<String,String>> signeesList = JsonPath.using(pathConfiguration)
                .parse(signees)
                .read("$.value.data.signeesList");
        if(signeesList == null || signeesList.isEmpty()) return false;
        return signeesList.stream().filter(s -> s.containsValue(personIdentifier)).findFirst().isPresent();
    }

    private boolean isAllowedApplicant(UserProfileDto userProfile, String applicant) {
        return userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.Admin) ||
               userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.User);
    }
    
    private boolean jsonExists(String json, String parameter) {
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        var result = JsonPath.using(pathConfiguration).parse(json).read(parameter);
        if (result != null) {
            if (result instanceof JSONArray) {
               return ((JSONArray) result).size() > 0;
            }
        }
        return false;
    }

}
