package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackage;
import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.formio.service.SubmissionService;
import com.jayway.jsonpath.Configuration;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MultiInstanceSignTaskCompleteListener extends AbstractTaskCompleteListener implements TaskListener {

    @Autowired
    public MultiInstanceSignTaskCompleteListener(FormService formService, SubmissionService submissionService,
                                                 Configuration jsonPathConfiguration,
                                                 MinioService minioService, CamundaProcessRepository processService,
                                                 ConfigurationProperties processConfProperties,
                                                 AuthenticationFacade authenticationFacade, UserService userService) {
        super(formService, submissionService, jsonPathConfiguration,
                minioService, processService, processConfProperties, authenticationFacade, userService);
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        processSubmission(delegateTask);
    }

    @Override
    Map<String, Object> getCurrentSubmission(DelegateTask delegateTask, String variableKey) {
        Map<String, Object> submission = getProcessVariableData(delegateTask, variableKey);
        return getCurrentUserVariableData(submission);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addAttachmentFiles(DelegateTask task, String documentKey, String formKey, List<MinioFile> minioFiles) {
        EDeliveryFilesPackage currentSignedFiles = (EDeliveryFilesPackage) Optional.ofNullable(task.getVariable(
                ProcessConstants.EDELIVERY_SIGNED_FILES_PACKAGE +
                        (documentKey != null ? documentKey : formKey))).orElse(null);

        if (currentSignedFiles != null) {
            for (MinioFile minioFile : minioFiles) {
                if (currentSignedFiles.getFiles().stream()
                        .noneMatch(file -> file.getFilename().equals(minioFile.getFilename()))) {
                    currentSignedFiles.getFiles().add(minioFile);
                }
            }
            task.setVariable(ProcessConstants.EDELIVERY_SIGNED_FILES_PACKAGE +
                            (documentKey != null ? documentKey : formKey),
                    currentSignedFiles);
        } else {
            task.setVariable(ProcessConstants.EDELIVERY_SIGNED_FILES_PACKAGE +
                            (documentKey != null ? documentKey : formKey),
                    new EDeliveryFilesPackage(true, minioFiles));
        }
    }

    @Override
    void addVariable(DelegateTask task, String formKey, Object data) {
        Map<String, Object> currentSubmissionData = getProcessVariableData(task, formKey);
        currentSubmissionData.put(userService.getPrincipalIdentifier(), data);
        task.setVariable(formKey, currentSubmissionData);
    }
}
