package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.service.formio.FormioConfigurationProperties;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service(value = "serviceSupplierServiceImpl")
@AllArgsConstructor
public class ServiceSupplierServiceImpl implements ServiceSupplierService {
    private final SubmissionService submissionService;
    private final ConfigurationProperties configurationProperties;
    private final static String ACTIVE = "active";
    private final long bulkSize = 100l;

    @Override
    public List<ResourceDto> getActiveSuppliers(String projectId, Authentication authentication) {
        return submissionService.getAllSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getSupplierResourceName()), authentication,
                Collections.singletonList(
                        new SubmissionFilter(
                                SubmissionFilterClauseEnum.NONE,
                                Collections.singletonMap(configurationProperties.getStatusPropertyKey(), ACTIVE))),
                bulkSize);
    }
}
