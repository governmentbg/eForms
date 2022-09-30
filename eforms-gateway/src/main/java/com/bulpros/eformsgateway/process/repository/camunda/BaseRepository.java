package com.bulpros.eformsgateway.process.repository.camunda;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public abstract class BaseRepository {
    protected static final String KEY = "key";
    protected static final String SLASH_SYMBOL = "/";

    @Autowired
    protected CamundaConfigurationProperties camundaConfProperties;
    @Autowired
    protected RestTemplate restTemplate;

    protected String getProcessInstanceUrl(Map<String, String> params) {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getProcessInstancePath());
        if(!params.isEmpty()){
            for (Map.Entry<String,String> param : params.entrySet()) {
                url.queryParam(param.getKey(), param.getValue());
            }
        }
        return url.toUriString();
    }

    protected String getHistoryProcessInstanceUrl(Map<String, String> params) {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getHistoryPath())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getProcessInstancePath());
        if(!params.isEmpty()){
            for (Map.Entry<String,String> param : params.entrySet()) {
                url.queryParam(param.getKey(), param.getValue());
            }
        }
        return url.toUriString();
    }

    protected String getHistoryVariableProcessesUrl(Map<String, String> params) {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getHistoryPath())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getVariableInstancePath());
        if(!params.isEmpty()){
            for (Map.Entry<String,String> param : params.entrySet()) {
                url.queryParam(param.getKey(), param.getValue());
            }
        }
        return url.toUriString();
    }

    private UriComponentsBuilder getProcessEngineRootUri() {
        return UriComponentsBuilder.fromHttpUrl(camundaConfProperties.getProcessEngineUrl())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getProcessEngineRestRoot());
    }

    protected String getStartInstanceUrl(String processKey) {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getProcessDefinitionResourcePath())
                .path(SLASH_SYMBOL)
                .path(KEY)
                .path(SLASH_SYMBOL)
                .path(processKey)
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getProcessDefintionStartInstanceResourcePath());

        return url.toUriString();
    }

    protected String getTaskUrl(String taskResourcePath, Map<String, Object> queryParams) {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(taskResourcePath);

        if (queryParams != null && !queryParams.isEmpty()) {
            queryParams.entrySet().stream().forEach(e -> url.queryParam(e.getKey(), e.getValue()));
        }

        return url.toUriString();
    }

    protected String getTerminateProcessUrl() {
        UriComponentsBuilder url = getProcessEngineRootUri()
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getMessageResourcePath());

        return url.toUriString();
    }

    protected String getPaymentCallbackUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(camundaConfProperties.getProcessEngineUrl())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getPaymentUrl())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getPaymentStatusCallbackResourcePath());

        return url.toUriString();
    }

    protected String getAdminCaseMessageUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(camundaConfProperties.getProcessEngineUrl())
                .path(SLASH_SYMBOL)
                .path(camundaConfProperties.getAdminCaseMessageUrl());

        return url.toUriString();
    }

    protected HttpHeaders createHttpAuthorizationHeader(String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + authorizationToken);
        return headers;
    }

}
