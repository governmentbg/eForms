package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.service.AuditlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("AuditlogCatchMessage")
@Slf4j
@RequiredArgsConstructor
public class AuditlogCatchMessage implements JavaDelegate {

    private final AuditlogService auditlogRegisterEventService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        AuditlogRequest auditlogRequest = (AuditlogRequest) delegateExecution.getVariable("auditlogRequest");
        if (auditlogRequest != null && auditlogRequest.getEventType() != null) {
            try {
                auditlogRegisterEventService.registerEvent(auditlogRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
