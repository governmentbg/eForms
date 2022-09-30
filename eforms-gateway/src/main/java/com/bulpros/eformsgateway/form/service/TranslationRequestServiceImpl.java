package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.model.TranslationRequest;
import com.bulpros.eformsgateway.form.utils.CommonUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.ResourceDataDto;
import com.bulpros.eformsgateway.form.web.controller.dto.TranslationRequestFilter;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TranslationRequestServiceImpl implements TranslationRequestService {


    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;

    public TranslationRequestServiceImpl(SubmissionService submissionService, ConfigurationProperties configuration) {
        this.submissionService = submissionService;
        this.configuration = configuration;
    }

    @Override
    public ResourceDto saveTranslationRequest(String projectId, Authentication authentication, ResourceDataDto<TranslationRequest> translationRequest) {

        ResourceDto newTranslationRequest = submissionService.createSubmission(
                new ResourcePath(projectId, configuration.getTranslationRequest()), authentication, CommonUtils.clone(translationRequest,String.class));

        return newTranslationRequest;
    }

    @Override
    public ResourceDto getTranslationRequestWithFilter(String projectId, Authentication authentication, TranslationRequestFilter translationRequestFilter, boolean filterByRequestId) {

        ArrayList<SubmissionFilter> filter = getTranslationRequestSubmissionFilters(translationRequestFilter, filterByRequestId);
        ResourcePath path = new ResourcePath(projectId, configuration.getTranslationRequest());
        var response = submissionService.getSubmissionsWithFilter(path, authentication, filter, 1L, 1L, "-modified");

        if(response.getElements().isEmpty()) return null;
        return response.getElements().get(0);
    }

    @NotNull
    private ArrayList<SubmissionFilter> getTranslationRequestSubmissionFilters(TranslationRequestFilter translationRequestFilter, boolean filterByRequestId) {
        var filter = new ArrayList<SubmissionFilter>();

        if (filterByRequestId) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getRequestId(), translationRequestFilter.getRequestId())));
        }

        if (!StringUtils.isEmpty(translationRequestFilter.getExternalReference())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getExternalReference(), translationRequestFilter.getExternalReference())));
        }

        if (!StringUtils.isEmpty(translationRequestFilter.getTargetLanguageCode())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getTargetLanguageCode(), translationRequestFilter.getTargetLanguageCode())));
        }

        if (!StringUtils.isEmpty(translationRequestFilter.getTranslatedText())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getTranslatedText(), translationRequestFilter.getTranslatedText())));
        }

        if (translationRequestFilter.getStatus() != null && !translationRequestFilter.getStatus().isEmpty()) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.IN,
                    Collections.singletonMap(configuration.getStatus(), translationRequestFilter.getStatus())));
        }
        return filter;
    }
}
