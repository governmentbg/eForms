package com.bulpros.formio.repository;

import java.util.List;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.utils.Page;

public interface SubmissionRepository {

    ResourceDto createSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            User user, String submissionData);

    ResourceDto updateSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            User user, String submissionId, String submissionData);

    ResourceDto deleteSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            User user, String submissionId);
    
    ResourceDto existsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, 
            User user, List<SubmissionFilter> filters);

    List<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                               User user, List<SubmissionFilter> filters, boolean selectDataOnly);

    Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                               User user, List<SubmissionFilter> filters, Long page, Long size,
                                               String sort, boolean selectDataOnly);

    List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            User user, Long bulkSize, boolean selectDataOnly);

    List<ResourceDto> getAllSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
                                                  User user, List<SubmissionFilter> filters, Long bulkSize, boolean selectDataOnly);

    SubmissionPdf downloadSubmission(String projectId, String id, User user, String submissionId, String filename);

    ResourceDto getSubmissionById(String projectId, ValueTypeEnum projectType, String form, ValueTypeEnum formType, User user, String submissionId);
}
