package com.bulpros.formio.repository.formio;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.SubmissionRepository;
import com.bulpros.formio.repository.client.FormioHttpClient;
import com.bulpros.formio.utils.Page;

@Component
public class SubmissionRepositoryImpl extends BaseRepository implements SubmissionRepository {


    @Override
    public ResourceDto createSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, User user, String submissionData) {
        var request = createRequestWithHeader(submissionData);

        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, 
                getSubmissionUrl(project, projectType, form, formType))
                .method(HttpMethod.POST)
                .entity(request)
                .returnType(new ParameterizedTypeReference<ResourceDto>(){})
                .build();
        return formioHttpClient.getResult(user).getBody();
    }

    @Override
    public ResourceDto updateSubmission (String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, User user, String submissionId, String submissionData) {
        var request = createRequestWithHeader(submissionData);

        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, 
                getPatchSubmissionUrl(project, projectType, form, formType, submissionId))
                .method(HttpMethod.PATCH)
                .returnType(new ParameterizedTypeReference<ResourceDto>(){})
                .entity(request)
                .build();
        return formioHttpClient.getResult(user).getBody();
    }

    @Override
    public ResourceDto deleteSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, User user, String submissionId) {
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, 
                getPatchSubmissionUrl(project, projectType, form, formType,submissionId))
                .method(HttpMethod.DELETE)
                .returnType(new ParameterizedTypeReference<ResourceDto>(){})
                .build();
        return formioHttpClient.getResult(user).getBody();
    }
    
    public ResourceDto existsWithFilter(String project, ValueTypeEnum projectType, String form, 
            ValueTypeEnum formType, User user, List<SubmissionFilter> filters) {
        
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, 
                getExistsUrlWithFilter(project, projectType, form, formType, filters))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<ResourceDto>(){})
                .build();
        return formioHttpClient.getResult(user).getBody();
        
    }

    @Override
    public List<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form,
                                                      ValueTypeEnum formType, User user, List<SubmissionFilter> filters,
                                                      boolean selectDataOnly) {
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                this.getSubmissionUrlWithFilter(project, projectType, form, formType, filters, selectDataOnly))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<List<ResourceDto>>(){})
                .build();
        return formioHttpClient.getListResult(user).getBody();
    }

    @Override
    public Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form,
                                                      ValueTypeEnum formType, User user, List<SubmissionFilter> filters,
                                                      Long page, Long size, String sort, boolean selectDataOnly) {
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                this.getSubmissionUrlWithFilterAndPages(project, projectType, form, formType, filters, page, size,
                        sort, selectDataOnly))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<List<ResourceDto>>(){})
                .build();
        return getResourceDtoPage(user, size);
    }

    private Page<ResourceDto> getResourceDtoPage(User user, Long size) {
        var responseEntity = this.formioHttpClient.getListResult(user);

        var submissions = (List<ResourceDto>) responseEntity.getBody();
        var totalElements = Long.valueOf(submissions.size());
        var totalPages = 1L;
        var contentRangeResponseHeader = responseEntity.getHeaders().get(HttpHeaders.CONTENT_RANGE);
        if (contentRangeResponseHeader != null && !contentRangeResponseHeader.isEmpty()) {
            var totalElementsStr = contentRangeResponseHeader.get(0)
                    .substring(contentRangeResponseHeader.get(0).indexOf(SLASH_SYMBOL) + 1);
            totalElements = Long.valueOf(totalElementsStr);
            if (size != null && size > 0) {
                totalPages = (totalElements % size == 0) ? (totalElements / size) : (totalElements / size) + 1;
            }
        }
        return new Page<>(totalPages, totalElements, submissions);
    }

    @Override
    public List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, 
            ValueTypeEnum formType, User user, Long bulkSize, boolean selectDataOnly) {
        return getAllSubmissionsWithFilter(project, projectType, form, formType, user, null, bulkSize, selectDataOnly);
    }

    @Override
    public List<ResourceDto> getAllSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form,
                                                         ValueTypeEnum formType, User user, List<SubmissionFilter> filters,
                                                         Long bulkSize, boolean selectDataOnly) {
        Long page = 1L;
        var submissionUrl = getSubmissionUrlWithFilterAndPages(project, projectType, form, formType,
                filters, page, bulkSize, null, selectDataOnly);

        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, submissionUrl)
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<List<ResourceDto>>() {
                })
                .build();
        var responseEntity = this.formioHttpClient.getListResult(user);
        List<ResourceDto> allSubmissions = responseEntity.getBody();
        while (responseEntity.getStatusCode().equals(HttpStatus.PARTIAL_CONTENT)) {

            submissionUrl = getSubmissionUrlWithFilterAndPages(project, projectType, form, formType,
                    filters, ++page, bulkSize, null, selectDataOnly);
            this.formioHttpClient = new FormioHttpClient.Builder(restTemplate, submissionUrl)
                    .method(HttpMethod.GET)
                    .returnType(new ParameterizedTypeReference<List<ResourceDto>>() {
                    })
                    .build();
            try {
                responseEntity = this.formioHttpClient.getListResult(user);
                allSubmissions.addAll(responseEntity.getBody());
            } catch (HttpClientErrorException exception) {
                if (exception.getRawStatusCode() == HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value()) {
                    break;
                } else {
                    throw exception;
                }
            }
        }
        return allSubmissions;
    }

    @Override
    public SubmissionPdf downloadSubmission(String projectId, String formId, User user, String submissionId, String filename) {
        var result = this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                getDownloadSubmissionUrlWithId(projectId, formId, submissionId))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<byte[]>(){})
                .build();
        ResponseEntity<Object> arrBytes = result.getObject(user);

        SubmissionPdf submissionPdf = new SubmissionPdf();
        submissionPdf.setContentType("application/pdf");
        submissionPdf.setContent((byte[]) arrBytes.getBody());
        submissionPdf.setFileName(filename);

        return submissionPdf;
    }

    @Override
    public ResourceDto getSubmissionById(String projectId, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                         User user, String submissionId) {
        this.formioHttpClient = new FormioHttpClient.Builder(restTemplate,
                getSubmissionUrlWithId(projectId, projectType, form, formType, submissionId))
                .method(HttpMethod.GET)
                .returnType(new ParameterizedTypeReference<ResourceDto>(){})
                .build();
        return formioHttpClient.getResult(user).getBody();
    }

    private HttpEntity<String> createRequestWithHeader(String submissionData){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        return new HttpEntity<>(submissionData, headers);
    }
}
