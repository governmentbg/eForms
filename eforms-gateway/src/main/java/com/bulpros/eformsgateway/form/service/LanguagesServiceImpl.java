package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LanguagesServiceImpl implements LanguagesService {


    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;

    public LanguagesServiceImpl(SubmissionService submissionService, ConfigurationProperties configuration, CacheService cacheService) {
        this.submissionService = submissionService;
        this.configuration = configuration;
    }


    @Override
    public List<ResourceDto> getLanguagesByStatus(String projectId, String status) {

        var languagesWithStatusFilter = new ArrayList<SubmissionFilter>();

        languagesWithStatusFilter.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Map.of(configuration.getStatus(), List.of(status))));

        Authentication authentication = AuthenticationService.createServiceAuthentication();

        ResourcePath path = new ResourcePath(projectId, configuration.getLanguages());

        return submissionService.getAllSubmissionsWithFilter(path, authentication, languagesWithStatusFilter, 100l, true);
    }

    @Override
    public ResourceDto getLanguageByLanguage(String projectId, Authentication authentication, String language) {

        var languageFilter = new ArrayList<SubmissionFilter>();

        languageFilter.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Map.of(configuration.getLanguage(), List.of(language))));

        ResourcePath path = new ResourcePath(projectId, configuration.getLanguages());

        var response = submissionService.getSubmissionsWithFilter(path, authentication, languageFilter);

        if(response.isEmpty()) {
            return null;
        }

        return response.get(0);
    }


}
