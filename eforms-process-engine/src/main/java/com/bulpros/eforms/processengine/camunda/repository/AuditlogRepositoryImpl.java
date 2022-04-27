package com.bulpros.eforms.processengine.camunda.repository;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.AuditlogServiceRequest;
import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component(value = "auditlogRepository")
@Slf4j
@RequiredArgsConstructor
public class AuditlogRepositoryImpl implements AuditlogRepository {

    private final ConfigurationProperties processConfProperties;
    private final RuntimeService runtimeService;

    @Override
    public void registerEvent(EventTypeEnum eventType, String eventDescription, DelegateExecution delegateExecution) {
        try {
            AuditlogRequest auditlogRequest = createEvent(eventType, eventDescription, delegateExecution);

            Execution execution = runtimeService.createExecutionQuery()
                    .messageEventSubscriptionName("AuditlogMessage")
                    .processInstanceId(delegateExecution.getProcessInstanceId())
                    .singleResult();

            if (execution != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("auditlogRequest", auditlogRequest);
                runtimeService.messageEventReceived("AuditlogMessage", execution.getId(), variables);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    @Override
    public AuditlogRequest createEvent(EventTypeEnum eventType, String eventDescription, DelegateExecution delegateExecution) {
        String documentRegId = delegateExecution.getProcessBusinessKey();
        Date eventTime = Calendar.getInstance().getTime();
        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        String serviceOID = (String) expressionManager.createExpression(processConfProperties.getServiceOIDPathExpr())
                .getValue(delegateExecution);
        String serviceName = (String) expressionManager.createExpression(processConfProperties.getServiceNamePathExpr())
                .getValue(delegateExecution);
        String adminiOID = (String) expressionManager.createExpression(processConfProperties.getSupplierOIDPathExpr())
                .getValue(delegateExecution);
        String adminLegalName = (String) expressionManager.createExpression(processConfProperties.getSupplierTitlePathExpr())
                .getValue(delegateExecution);

        AuditlogServiceRequest auditlogServiceRequest =
                new AuditlogServiceRequest(serviceOID, null, serviceName,
                        null, adminiOID, adminLegalName);

        return new AuditlogRequest(eventTime, eventType,
                eventDescription, auditlogServiceRequest, null, null, documentRegId);
    }
}
