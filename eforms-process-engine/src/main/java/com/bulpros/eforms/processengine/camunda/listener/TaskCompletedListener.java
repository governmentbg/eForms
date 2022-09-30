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

@Component
public class TaskCompletedListener extends AbstractTaskCompleteListener implements TaskListener {

    @Autowired
    public TaskCompletedListener(FormService formService, SubmissionService submissionService,
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
        return getProcessVariableData(delegateTask, variableKey);
    }

    @Override
    protected void addAttachmentFiles(DelegateTask task, String documentKey, String formKey, List<MinioFile> minioFiles) {
        EDeliveryFilesPackage eDeliveryFilesPackage = new EDeliveryFilesPackage(false, minioFiles);
        task.setVariable(ProcessConstants.EDELIVERY_ATTACHMENTS +
                        (documentKey != null ? documentKey : formKey),
                eDeliveryFilesPackage);
    }

    @Override
    void addVariable(DelegateTask task, String formKey, Object data) {
        task.setVariable(formKey, data);
    }
}
