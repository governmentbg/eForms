package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.enums.GenerateFilesParticipantType;
import com.bulpros.eforms.processengine.camunda.model.enums.NonRequiredDocumentStatusEnum;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.service.GenerateFilesPackageService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;

import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
@Named("generateFilesPackage")
@Scope("prototype")
public class GenerateFilesPackageDelegate implements JavaDelegate {

    private final AuthenticationFacade authenticationFacade;
    private final CamundaProcessRepository processRepository;
    private final ConfigurationProperties processConfProperties;
    private final Configuration jsonPathConfiguration;
    private final GenerateFilesPackageService generateFilesPackageService;

    private Expression participantType;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        GenerateFilesParticipantType participantType = GenerateFilesParticipantType.valueOf(
                (String) this.getParticipantType().getValue(delegateExecution));
        if (participantType.equals(GenerateFilesParticipantType.CITIZEN)) {
            Authentication authentication = this.authenticationFacade.getAuthentication();
            ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            String businessKey = this.processRepository.getBusinessKey(delegateExecution.getProcessInstanceId());
            String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                    .getValue(delegateExecution);
            String arId = (String) expressionManager.createExpression(processConfProperties.getArIdPathExpr())
                    .getValue(delegateExecution);
            Map<String, Object> processVariables = delegateExecution.getVariables();
            boolean processHasSigning = false;
            Map<String, Object> formSubmissionsSubjectToFilesGeneration = new HashMap<>();
            for (Map.Entry<String, Object> processVariable : processVariables.entrySet()) {
                if (processVariable.getKey().contains(ProcessConstants.SUBMISSION_DATA + arId)) {
                    JacksonJsonNode submissionJson = (JacksonJsonNode) Spin.JSON(processVariable.getValue());
                    DocumentContext submissionContext = JsonPath.using(jsonPathConfiguration)
                            .parse(submissionJson.toString());
                    String isDocumentRequired = submissionContext.read(processConfProperties.getIsDocumentRequired());
                    Boolean filesPackageIsSignable = submissionContext.read(
                            processConfProperties.getFormIsSignableJsonPathQuery());
                    Boolean generateFilesPackage = submissionContext.read(
                            processConfProperties.getFormGenerateFilesPackageJsonPathQuery());

                    if (filesPackageIsSignable != null && filesPackageIsSignable) {
                        processHasSigning = true;
                    }
                    if (generateFilesPackage != null && generateFilesPackage) {
                        formSubmissionsSubjectToFilesGeneration.put(processVariable.getKey(), processVariable.getValue());
                    }

                    if (nonNull(isDocumentRequired) && !isDocumentRequired.equals("")) {
                        var isRequiredStatus =
                                NonRequiredDocumentStatusEnum.getEnumByStatus(isDocumentRequired);
                        if (nonNull(isRequiredStatus) &&
                                ((nonNull(generateFilesPackage) && generateFilesPackage) || (
                                        nonNull(filesPackageIsSignable) && filesPackageIsSignable))) {
                            log.warn("Document is not required");
                            throw new EFormsProcessEngineException(SeverityEnum.WARN, "DOCUMENT_IS_NOT_REQUIRED");
                        }
                    }
                }
            }

            delegateExecution.setVariable(ProcessConstants.PROCESS_HAS_SIGNING, processHasSigning);

            generateFilesPackageService.generate(authentication, projectId, arId,
                    businessKey, formSubmissionsSubjectToFilesGeneration, participantType,
                    delegateExecution);

        } else {
            Authentication authentication = this.authenticationFacade.getAuthentication();
            ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            String businessKey = this.processRepository.getBusinessKey(delegateExecution.getProcessInstanceId());
            String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                    .getValue(delegateExecution);
            String arId = (String) expressionManager.createExpression(processConfProperties.getArIdPathExpr())
                    .getValue(delegateExecution);
            Map<String, Object> processVariables = delegateExecution.getVariables();
            Map<String, Object> formSubmissionsSubjectToFilesGeneration = new HashMap<>();
            for (Map.Entry<String, Object> processVariable : processVariables.entrySet()) {
                if (processVariable.getKey().contains(ProcessConstants.SUBMISSION_DATA + arId + ProcessConstants.FORMA_RESPONSE_SUFFIX)) {
                    formSubmissionsSubjectToFilesGeneration.put(processVariable.getKey(), processVariable.getValue());
                }
            }

            generateFilesPackageService.generate(authentication, projectId, arId,
                    businessKey, formSubmissionsSubjectToFilesGeneration, participantType,
                    delegateExecution);
        }
    }
}
