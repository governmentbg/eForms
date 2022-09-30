package com.bulpros.formio.service.formio;

import java.util.List;

import io.micrometer.core.annotation.Timed;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.SubmissionPdf;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.SubmissionRepository;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.security.FormioUserService;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.utils.Page;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final FormioUserService userService;

    @Override
    @Timed(value = "formio-client-create-submission-with-path.time")
    public ResourceDto createSubmission(ResourcePath path, Authentication authentication, String submissionData) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.createSubmission(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, submissionData);
    }

    @Override
    @Timed(value = "formio-client-create-submission-with-project.time")
    public ResourceDto createSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, String submissionData) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.createSubmission(project, projectType, form, formType, user, submissionData);
    }

    @Override
    public ResourceDto updateSubmission(ResourcePath path, Authentication authentication, String submissionId, String submissionData) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.updateSubmission(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, submissionId, submissionData);
    }
    
    @Override
    public ResourceDto updateSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, String submissionId, String submissionData) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.updateSubmission(project, projectType, form, formType, user, submissionId, submissionData);
    }
    
    @Override
    public ResourceDto deleteSubmission(ResourcePath path, Authentication authentication, String submissionId) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.deleteSubmission(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, submissionId);
    }
    
    @Override
    public ResourceDto deleteSubmission(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, String submissionId) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.deleteSubmission(project, projectType, form, formType, user, submissionId);
    }
    
    @Override
    public ResourceDto existsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType,
            Authentication authentication, List<SubmissionFilter> filters) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.existsWithFilter(project, projectType, form, formType, user, filters);
    }
    
    @Override
    public ResourceDto existsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.existsWithFilter(path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters);
    }
    
    @Override
    public List<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters,true);
    }

    @Override
    public List<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, 
            ValueTypeEnum formType, Authentication authentication, List<SubmissionFilter> filters) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(project, projectType, form, formType, user, filters, true);
    }
    
    @Override
    public Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, 
            ValueTypeEnum formType, Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(project, projectType, form, formType, user, filters, page, size, sort, true);
    }
    
    @Override
    public Page<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters, page, size, sort, true);
    }

    @Override
    public Page<ResourceDto> getSubmissionsWithFilter(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort, boolean selectDataOnly) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(project, projectType, form, formType, user, filters, page, size, sort, selectDataOnly);
    }

    @Override
    public Page<ResourceDto> getSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long page, Long size, String sort, boolean selectDataOnly) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionsWithFilter(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters, page, size, sort, selectDataOnly);
    }

    @Override
    public List<ResourceDto> getAllSubmissions(ResourcePath path, Authentication authentication, Long bulkSize) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissions(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, bulkSize, true);
    }
    
    @Override
    public List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, Long bulkSize) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissions(project, projectType, form, formType, user, bulkSize, true);
    }

    @Override
    public List<ResourceDto> getAllSubmissions(ResourcePath path, Authentication authentication, Long bulkSize, boolean selectDataOnly) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissions(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, bulkSize, true);
    }

    @Override
    public List<ResourceDto> getAllSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long bulkSize, boolean selectDataOnly) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissionsWithFilter(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters, bulkSize, selectDataOnly);
    }

    @Override
    public List<ResourceDto> getAllSubmissions(String project, ValueTypeEnum projectType, String form, ValueTypeEnum formType, Authentication authentication, Long bulkSize, boolean selectDataOnly) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissions(project, projectType, form, formType, user, bulkSize, true);
    }

    @Override
    public List<ResourceDto> getAllSubmissionsWithFilter(ResourcePath path, Authentication authentication, List<SubmissionFilter> filters, Long bulkSize) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getAllSubmissionsWithFilter(
                path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, filters, bulkSize, true);
    }

    @Override
    public SubmissionPdf downloadSubmissionById(String projectId, String id, Authentication authentication, String submissionId, String filename) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.downloadSubmission(projectId, id, user, submissionId, filename);
    }

    @Timed(value = "formio-client-get-submission-by-id.time")
    @Override
    public ResourceDto getSubmissionById(ResourcePath path, Authentication authentication, String submissionId) {
        User user = this.userService.getUser(authentication);
        return submissionRepository.getSubmissionById(path.getProject(), path.getProjectType(), path.getForm(), path.getFormType(), user, submissionId);
    }
}
