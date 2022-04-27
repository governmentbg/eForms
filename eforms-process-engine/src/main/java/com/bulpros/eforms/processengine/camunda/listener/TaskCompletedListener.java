package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackage;
import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.spin.Spin;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TaskCompletedListener extends AbstractTaskCompleteListener implements TaskListener {

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
                Spin.JSON(eDeliveryFilesPackage));
    }

    @Override
    void addVariable(DelegateTask task, String formKey, Object data) {
        task.setVariable(formKey, data);
    }
}
