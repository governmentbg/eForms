package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.FormioFile;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import spinjar.com.jayway.jsonpath.DocumentContext;
import spinjar.com.jayway.jsonpath.JsonPath;
import spinjar.com.jayway.jsonpath.internal.JsonContext;
import spinjar.com.minidev.json.JSONArray;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public abstract class AbstractTaskCompleteListener {

    @Autowired
    FormService formService;
    @Autowired
    SubmissionService submissionService;
    @Autowired
    MinioService minioService;
    @Autowired
    CamundaProcessRepository processService;
    @Autowired
    ConfigurationProperties processConfProperties;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    UserService userService;

    void processSubmission(DelegateTask delegateTask) {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        try {
            TaskFormData taskFormData = this.formService.getTaskFormData(delegateTask.getId());
            String formKey = taskFormData.getFormKey();
            String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(formKey);
            String formApiPath = EFormsUtils.getFormApiPath(formKey);
            String businessKey = this.processService.getBusinessKey(delegateTask.getProcessInstanceId());
            ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            String projectId = (String) expressionManager
                    .createExpression(processConfProperties.getProjectIdPathExpr())
                    .getValue(delegateTask.getExecution());
            String requestor = Objects.toString(delegateTask.getVariable(ProcessConstants.INITIATOR), null);
            String applicant = Objects.toString(expressionManager
                    .createExpression(processConfProperties.getApplicantPathExpr())
                    .getValue(delegateTask.getExecution()), null);
            String supplier = Objects.toString(expressionManager
                    .createExpression(processConfProperties.getSupplierPathExpr())
                    .getValue(delegateTask.getExecution()), null);

            var localVariables = delegateTask.getVariablesLocal();
            var embeddedFormLocalVariable = localVariables.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().endsWith(ProcessConstants.EMBEDDED_SUFFIX))
                    .collect(Collectors.toMap(e->e.getKey(),e->e.getValue()));
            if (embeddedFormLocalVariable.isEmpty()) {
                ResourceDto submission = createSubmission(delegateTask, projectId, authentication,
                formApiPath, null, formDataSubmissionKey,
                        businessKey, requestor, applicant, supplier);
                addVariable(delegateTask, ProcessConstants.SUBMISSION_ID + formDataSubmissionKey, submission.get_id());
            } else {
                for (Map.Entry<String, Object> localVariable : delegateTask.getVariablesLocal().entrySet()) {
                    delegateTask.getExecution().setVariable(localVariable.getKey(), localVariable.getValue());
                    if (EFormsUtils.isLocalVariableFormSubmission(localVariable.getKey())) {
                        ResourceDto nestedSubmission = createSubmission(delegateTask, projectId, authentication,
                                EFormsUtils.getFormApiPathFromSubmissionKey(localVariable.getKey()),
                                formDataSubmissionKey,
                                EFormsUtils.getFormDataSubmissionKeyFromVariableName(localVariable.getKey()),
                                businessKey, requestor, applicant, supplier);

                        addVariable(delegateTask, ProcessConstants.SUBMISSION_ID +
                                        EFormsUtils.getFormDataSubmissionKeyFromVariableName(localVariable.getKey()),
                                nestedSubmission.get_id());
                    }
                }
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(exception.getMessage());
        }
    }

    abstract Map<String, Object> getCurrentSubmission(DelegateTask delegateTask, String formDataSubmissionKey);

    abstract void addAttachmentFiles(DelegateTask task, String documentKey, String formKey, List<MinioFile> minioFiles);

    abstract void addVariable(DelegateTask task, String formKey, Object data);

    @SuppressWarnings("unchecked")
    Map<String, Object> getProcessVariableData(DelegateTask task, String variableKey) {
        var result = (Map<String, Object>) ((TaskEntity) task).getProcessInstance().getVariable(variableKey);
        if(nonNull(result)){
            return result;
        }
        return Optional.ofNullable(task.getVariable(variableKey))
                .map(obj -> (HashMap<String, Object>) obj)
                .orElse(new HashMap<>());
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> getCurrentUserVariableData(Map<String, Object> data) {
        return (Map<String, Object>)
                Optional.ofNullable(data.get(userService.getPrincipalIdentifier()))
                        .orElse(data);
    }

    JacksonJsonNode processStorage(JacksonJsonNode node, String projectId, String businessKey,
                                   String documentDataSubmissionKey, String formDataSubmissionKey,
                                   List<MinioFile> minioFiles) throws Exception {
        JSONArray jsonArray = JsonPath.read(node.toString(), "$..[?(@.storage)]");
        if (!jsonArray.isEmpty()) {
            FormioFile[] formioFiles = new ObjectMapper().readValue(jsonArray.toString(), FormioFile[].class);
            DocumentContext context = JsonPath.parse(node.toString());
            for (FormioFile formioFile : formioFiles) {
                MinioFile minioFile = null;
                if ("base64".equals(formioFile.getStorage())) {
                    String base64EncodeFile = formioFile.getUrl().substring(formioFile.getUrl().lastIndexOf(",") + 1);
                    byte[] decodedFile = Base64.getDecoder().decode(base64EncodeFile);
                    minioFile = this.minioService.saveFile(formioFile.getOriginalName(), decodedFile, formioFile.getType(),
                            projectId, businessKey, documentDataSubmissionKey, formDataSubmissionKey);
                    context.put("$..[?(@.storage=='base64')][?(@.name=='" + formioFile.getName() + "')]", "url", minioFile.getLocation());
                    context.put("$..[?(@.storage=='base64')][?(@.name=='" + formioFile.getName() + "')]", "storage", "url");
                } else {
                    minioFile = new MinioFile(formioFile.getOriginalName(), null, formioFile.getType(), formioFile.getUrl());
                }

                minioFiles.add(minioFile);
            }

            return (JacksonJsonNode) Spin.JSON(context.json());
        } else {
            return node;
        }
    }

    ResourceDto createSubmission(DelegateTask delegateTask, String projectId, Authentication authentication,
                                 String formApiPath, String documentDataSubmissionKey, String formDataSubmissionKey,
                                 String businessKey, String requestor, String applicant, String supplier) throws Exception {

        Map<String, Object> currentSubmission = getCurrentSubmission(delegateTask,
                ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey);
        JacksonJsonNode submissionJson = (JacksonJsonNode) Spin.JSON(currentSubmission);

        JsonContext contextContext = (JsonContext) JsonPath.parse(submissionJson.toString());
        if (businessKey != null) {
            contextContext.put("$.data", "businessKey", businessKey);
        }
        if (requestor != null) {
            contextContext.put("$.data", "requestor", requestor);
        }
        if (applicant != null) {
            contextContext.put("$.data", "applicant", applicant);
        }
        if (supplier != null) {
            contextContext.put("$.data", "supplier", supplier);
        }

        submissionJson = (JacksonJsonNode) Spin.JSON(contextContext.json());

        List<MinioFile> minioFiles = new ArrayList<>();
        submissionJson = processStorage(submissionJson, projectId, businessKey, documentDataSubmissionKey,
                formDataSubmissionKey, minioFiles);

        if (!minioFiles.isEmpty()) {
            addAttachmentFiles(delegateTask, documentDataSubmissionKey, formDataSubmissionKey, minioFiles);
            JsonContext jsonContext = (JsonContext) JsonPath.parse(submissionJson.toString())
                    .delete("$..[?(@.storage)]");
            addVariable(delegateTask, ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey, jsonContext.json());
        }
        return submissionService.createSubmission(new ResourcePath(projectId, formApiPath), authentication, submissionJson.toString());
    }

}
