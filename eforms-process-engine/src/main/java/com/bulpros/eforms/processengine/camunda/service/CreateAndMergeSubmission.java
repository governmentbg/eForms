package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.formio.dto.ResourceDto;
import org.springframework.security.core.Authentication;

public interface CreateAndMergeSubmission<T> {
    void mergeSubmissions(String projectId, String resourceName, ResourceDto submission,
                          T details, String[] removeFields, Authentication authentication) throws Exception;
    void createSubmission(String projectId, String resourceName, T details, Authentication authentication);
}
