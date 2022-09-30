package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.enums.NonRequiredDocumentStatusEnum;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.FormioClientException;
import com.bulpros.formio.model.FormioFile;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Component
public abstract class AbstractTaskCompleteListener {

    protected final FormService formService;
    protected final SubmissionService submissionService;
    protected final Configuration jsonPathConfiguration;
    protected final MinioService minioService;
    protected final CamundaProcessRepository processService;
    protected final ConfigurationProperties processConfProperties;
    protected final AuthenticationFacade authenticationFacade;
    protected final UserService userService;

    public AbstractTaskCompleteListener(FormService formService, SubmissionService submissionService,
                                        Configuration jsonPathConfiguration,
                                        MinioService minioService, CamundaProcessRepository processService,
                                        ConfigurationProperties processConfProperties,
                                        AuthenticationFacade authenticationFacade, UserService userService) {
        this.formService = formService;
        this.submissionService = submissionService;
        this.jsonPathConfiguration = jsonPathConfiguration;
        this.minioService = minioService;
        this.processService = processService;
        this.processConfProperties = processConfProperties;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

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
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        } catch (EFormsProcessEngineException exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "COMPLETE_TASK", exception.getMessage());
        }
    }

    abstract Map<String, Object> getCurrentSubmission(DelegateTask delegateTask, String formDataSubmissionKey);

    abstract void addAttachmentFiles(DelegateTask task, String documentKey, String formKey, List<MinioFile> minioFiles);

    abstract void addVariable(DelegateTask task, String formKey, Object data);

    @SuppressWarnings("unchecked")
    Map<String, Object> getProcessVariableData(DelegateTask task, String variableKey) {
        var result = (Map<String, Object>) ((TaskEntity) task).getProcessInstance().getVariable(variableKey);
        if (nonNull(result)) {
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

    DocumentContext processStorage(DocumentContext submissionContext, String projectId, String businessKey,
                                   String documentDataSubmissionKey, String formDataSubmissionKey,
                                   List<MinioFile> minioFiles) throws Exception {
        JSONArray jsonArray = submissionContext.read("$..[?(@.storage)]");
        if (!jsonArray.isEmpty()) {
            FormioFile[] formioFiles = new ObjectMapper().readValue(jsonArray.toString(), FormioFile[].class);
            for (FormioFile formioFile : formioFiles) {
                minioFiles.add(new MinioFile(formioFile.getOriginalName(), null, formioFile.getType(),
                        formioFile.getUrl()));
            }
        }
        return submissionContext;
    }

    ResourceDto createSubmission(DelegateTask delegateTask, String projectId, Authentication authentication,
                                 String formApiPath, String documentDataSubmissionKey, String formDataSubmissionKey,
                                 String businessKey, String requestor, String applicant, String supplier) throws Exception {

        try {
            Map<String, Object> currentSubmission = getCurrentSubmission(delegateTask,
                    ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey);
            JacksonJsonNode submissionJson = (JacksonJsonNode) Spin.JSON(currentSubmission);
            DocumentContext submissionContext = JsonPath.using(jsonPathConfiguration).parse(submissionJson.toString());

            validateIsDocumentRequired(submissionContext);

            if (businessKey != null) {
                submissionContext.put("$.data", "businessKey", businessKey);
            }
            if (requestor != null) {
                submissionContext.put("$.data", "requestor", requestor);
            }
            if (applicant != null) {
                submissionContext.put("$.data", "applicant", applicant);
            }
            if (supplier != null) {
                submissionContext.put("$.data", "supplier", supplier);
            }

            List<MinioFile> minioFiles = new ArrayList<>();
            submissionContext = processStorage(submissionContext, projectId, businessKey, documentDataSubmissionKey,
                    formDataSubmissionKey, minioFiles);

            if (!minioFiles.isEmpty()) {
                addAttachmentFiles(delegateTask, documentDataSubmissionKey, formDataSubmissionKey, minioFiles);
            }
            return submissionService.createSubmission(new ResourcePath(projectId, formApiPath), authentication, submissionContext.jsonString());
        } catch (FormioClientException exception) {
            if(exception.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
                throw new EFormsProcessEngineException(SeverityEnum.ERROR, "FORMIO.UNAVAILABLE", exception.getData());
            }
            else {
                throw new EFormsProcessEngineException(SeverityEnum.ERROR, "FORMIO.COMMUNICATION", exception.getData());
            }
        }
    }

    private void validateIsDocumentRequired(DocumentContext submissionContext) {
        String isDocumentRequired = submissionContext.read(processConfProperties.getIsDocumentRequired());
        Boolean filesPackageIsSignable = submissionContext.read(processConfProperties.getFormIsSignableJsonPathQuery());
        Boolean generateFilesPackage = submissionContext.read(processConfProperties.getFormGenerateFilesPackageJsonPathQuery());

        if (nonNull(isDocumentRequired) && !isDocumentRequired.equals("")) {
            var isRequiredStatus =
                    NonRequiredDocumentStatusEnum.getEnumByStatus(isDocumentRequired);
            if (nonNull(isRequiredStatus) &&
                    ((nonNull(generateFilesPackage) && generateFilesPackage) || (nonNull(filesPackageIsSignable) && filesPackageIsSignable))) {
                log.warn("Document is not required");
                throw new EFormsProcessEngineException(SeverityEnum.WARN, "DOCUMENT_IS_NOT_REQUIRED");
            }
        }
    }

}
