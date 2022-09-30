package com.bulpros.formio.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.utils.Page;

public interface SubmissionService {
    ResourceDto createSubmission(ResourcePath path, Authentication authentication, String submissionData);
    
    ResourceDto createSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, String submissionData);

    ResourceDto updateSubmission(ResourcePath path, Authentication authentication, String submissionId, String submissionData);
    
    ResourceDto updateSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, String submissionId, String submissionData);
    
    ResourceDto deleteSubmission(ResourcePath path, Authentication authentication, String submissionId);
    
    ResourceDto deleteSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, String submissionId);
    
    ResourceDto existsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters);
    
    ResourceDto existsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, List<SubmissionFilter> filters);

    List<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, 
            Authentication authentication, List<SubmissionFilter> filters);
    
    List<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters);

    Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, 
            Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort);
    
    Page<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort);

    Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                               Authentication authentication, List<SubmissionFilter> filters, Long page,
                                               Long size, String sort, boolean selectDataOnly);

    Page<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters,
                                               Long page, Long size, String sort, boolean selectDataOnly);

    List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, Long pageContentSize);
    
    List<ResourceDto> getAllSubmissions(ResourcePath path, Authentication authentication, Long bulkSize);

    List<ResourceDto> getAllSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long bulkSize);

    List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                        Authentication authentication, Long pageContentSize, boolean selectDataOnly);

    List<ResourceDto> getAllSubmissions(ResourcePath path, Authentication authentication, Long bulkSize, boolean selectDataOnly);

    List<ResourceDto> getAllSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long bulkSize, boolean selectDataOnly);

    SubmissionPdf downloadSubmissionById(String projectId, String id,
                                         Authentication authentication, String submissionId, String filename);
    ResourceDto getSubmissionById(ResourcePath path, Authentication authentication, String submissionId);
}
