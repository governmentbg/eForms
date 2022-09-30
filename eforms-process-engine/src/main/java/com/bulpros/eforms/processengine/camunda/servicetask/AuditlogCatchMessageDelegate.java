package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.eforms.processengine.camunda.service.AuditlogService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Slf4j
@RequiredArgsConstructor
@Named("AuditlogCatchMessage")
public class AuditlogCatchMessageDelegate implements JavaDelegate {

    private final AuditlogService auditlogService;
    @Override
    public void execute(DelegateExecution delegateExecution) {
        AuditlogRequest auditlogRequest = (AuditlogRequest) delegateExecution.getVariable("auditlogRequest");
        if (auditlogRequest != null && auditlogRequest.getEventType() != null) {
            try {
                auditlogService.registerEvent(auditlogRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new EFormsProcessEngineException(SeverityEnum.ERROR, "AUDITLOG.UNAVAILABLE", e.getMessage());
            }
        }
    }
}
