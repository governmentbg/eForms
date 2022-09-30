package com.bulpros.formio.repository.formio;

import com.bulpros.formio.exception.IllegalFormioArgumentException;
import com.bulpros.formio.repository.client.FormioHttpClient;
import com.bulpros.formio.service.formio.FormioConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public abstract class BaseRepository {
    protected static final String SLASH_SYMBOL = "/";
    protected FormioHttpClient formioHttpClient;
    @Autowired
    protected FormioConfigurationProperties formioConfProperties;
    @Autowired
    protected RestTemplate restTemplate;

    private UriComponentsBuilder getBaseUrl(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType){
        var url = UriComponentsBuilder.fromHttpUrl(formioConfProperties.getUrl());
        switch(projectType){
            case ID:
                url.path("/project" + SLASH_SYMBOL + project);
                break;
            case PATH:
                url.path(SLASH_SYMBOL + project);
                break;
        }
        switch(formType){
            case ID:
                url.path("/form" + SLASH_SYMBOL + form);
                break;
            case PATH:
                url.path(SLASH_SYMBOL + form);
                break;
        }
        return url;
    }

    protected URI getResourceUrl(String projectId, String resourcePath) {
        var url = this.getBaseUrl(projectId, ValueTypeEnum.ID, resourcePath, ValueTypeEnum.PATH);
        return url.build().toUri();
    }

    protected URI getSubmissionUrlWithId(String project, String form ) {
        return this.getSubmissionUrl(project, ValueTypeEnum.ID, form, ValueTypeEnum.ID);
    }

    protected URI getSubmissionUrlWithPath(String projectId, String path){
        return this.getSubmissionUrl(projectId, ValueTypeEnum.ID, path, ValueTypeEnum.PATH);
    }

    protected URI getSubmissionUrl(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType){
        var url = this.getBaseUrl(project, projectType, form, formType)
                .path("/submission");
        return url.build().toUri();
    }

    protected URI getSubmissionUrlWithId(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, String submissionId){
        var url = this.getBaseUrl(project, projectType, form, formType)
                .path("/submission" + SLASH_SYMBOL + submissionId);
        return url.build().toUri();
    }

    protected URI getDownloadSubmissionUrlWithId(String projectId, String formId, String submissionId) {
        var url = this.getDownloadSubmissionUrl(projectId, ValueTypeEnum.ID, formId, ValueTypeEnum.ID, submissionId);
        return url;
    }

    protected URI getDownloadSubmissionUrl(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, String submissionId) {
        var url = this.getBaseUrl(project, projectType, form, formType)
                .path("/submission" + SLASH_SYMBOL + submissionId)
                .path("/download");
        return url.build().toUri();
    }

    protected URI getFormUrl(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType) {
        var url = this.getBaseUrl(project, projectType, form, formType);
        return url.build().toUri();
    }

    protected URI getCreatePdfWithSubmissionUrl(String projectId) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(formioConfProperties.getPdfUrl())
                .path("/pdf" + SLASH_SYMBOL + projectId)
                .path("/download");
        return url.build().toUri();
    }

    protected URI getPatchSubmissionUrl(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, String submissionId) {
        var url = this.getBaseUrl(project, projectType, form, formType)
            .path("/submission")
            .path(SLASH_SYMBOL + submissionId);
        return url.build().toUri();
    }

    protected URI getPatchSubmissionUrlByPath(String project, String form, String submissionId) {
        return this.getPatchSubmissionUrl(project, ValueTypeEnum.ID, form, ValueTypeEnum.PATH, submissionId);
    }
    
    protected URI getExistsUrlWithFilter(String projectId, ValueTypeEnum projectType, String resourcePath, 
            ValueTypeEnum resourceType, List<SubmissionFilter> filters) {
        var url = this.getBaseUrl(projectId, projectType, resourcePath, resourceType)
                .path("/exists");
        readFilters(filters, null, null, null, url);
        
        return url.build().toUri();
    }

    protected URI getSubmissionUrlWithFilter(String projectId, ValueTypeEnum projectType, String resourcePath,
            ValueTypeEnum resourceType, List<SubmissionFilter> filters, boolean selectDataOnly) {
        return getSubmissionUrlWithFilterAndPages(projectId, projectType, resourcePath,
                resourceType, filters, null, null, null, selectDataOnly);
    }

    protected URI getSubmissionUrlWithFilter(String projectId, ValueTypeEnum projectType, String resourcePath,
                                             ValueTypeEnum resourceType, List<SubmissionFilter> filters) {
        return getSubmissionUrlWithFilterAndPages(projectId, projectType, resourcePath,
                resourceType, filters, null, null, null, true);
    }

    protected URI getSubmissionUrlWithFilterAndPages(String projectId, ValueTypeEnum projectType, String resourcePath, 
            ValueTypeEnum resourceType, List<SubmissionFilter> filters, Long page, Long size, String sort, boolean selectDataOnly) {
        
        var url = this.getBaseUrl(projectId, projectType, resourcePath, resourceType)
                .path("/submission")
                .queryParam("select", "modified");
        if(selectDataOnly) {
            url.queryParam("select", "data");
        }
        readFilters(filters, page, size, sort, url);
        
        return url.build().toUri();
    }

    protected URI getSubmissionUrlWithFilterAndPages(String projectId, ValueTypeEnum projectType, String resourcePath,
                                                     ValueTypeEnum resourceType, List<SubmissionFilter> filters,
                                                     Long page, Long size, String sort) {
        return getSubmissionUrlWithFilterAndPages(projectId, projectType, resourcePath,
                resourceType, filters, page, size, sort, true);
    }

    protected UriComponentsBuilder readFilters(List<SubmissionFilter> filters, 
            Long page, Long size, String sort, UriComponentsBuilder url) {
        if (filters != null && filters.size() > 0) {
            for (SubmissionFilter f : filters) {
                var properties = f.getProperties();
                for (var entry : properties.entrySet()) {
                    var fieldKey = entry.getKey();
                    var fieldValues = entry.getValue();
                    if (fieldValues != null) {
                        var fieldValuesString = fieldValues.toString();
                        if (fieldValues instanceof List) {
                            List<String> stringValues = ((List<?>) fieldValues)
                                    .stream()
                                    .filter(v -> v != null)
                                    .map(v -> v.toString())
                                    .collect(Collectors.toList());
                            fieldValuesString = String.join(",", stringValues);
                        }
                        var prefix = "data.";
                        if (SubmissionFilterReservedParamsEnum.getByValue(fieldKey) != null || !f.isSubmissionData()) {
                            prefix = "";
                        }
                        url.queryParam(prefix + fieldKey + f.getClause().getValue(), fieldValuesString);
                    } else {
                        log.error(
                                "Unable to get submission(s) by properties: {}. No property values are passed for property: {}.",
                                properties.keySet(), fieldKey);
                        throw new IllegalFormioArgumentException("Unable to get submission(s) by properties: " + properties.keySet()
                                + ". No property values are passed for property: " + fieldKey);
                    }
                }
            }
        }

        if (size != null && size > 0) {
            url.queryParam("limit", size);
        }
        if (page != null && page > 0 && size != null) {
            var skip = (page - 1) * size;
            url.queryParam("skip", skip);
        }
        if (sort != null && !sort.isEmpty()) {
            url.queryParam("sort", sort);
        }
        
        return url;
    }
}
