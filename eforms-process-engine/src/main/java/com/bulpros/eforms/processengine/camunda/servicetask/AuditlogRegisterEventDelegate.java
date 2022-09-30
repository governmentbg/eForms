package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.eforms.processengine.camunda.repository.AuditlogRepository;
import com.bulpros.eforms.processengine.camunda.service.AuditlogService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Named("AuditlogRegisterEvent")
public class AuditlogRegisterEventDelegate implements JavaDelegate {

    private final AuditlogRepository auditlogRepository;
    private final AuditlogService auditlogService;

    private Expression eventType;
    private Expression eventDescription;

    @Override
    public void execute(DelegateExecution delegateExecution) {

        EventTypeEnum eventType = this.getEventType() != null ?
                EventTypeEnum.valueOf((String) this.getEventType().getValue(delegateExecution)) : null;
        String eventDescription = this.getEventDescription() != null ?
                (String) this.getEventDescription().getValue(delegateExecution) : null;

        if (eventType != null) {
            AuditlogRequest auditlogRequest = auditlogRepository.createEvent(eventType, eventDescription, delegateExecution);
            try {
                auditlogService.registerEvent(auditlogRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new EFormsProcessEngineException(SeverityEnum.ERROR, "AUDITLOG.UNAVAILABLE",  e.getMessage());
            }
        }
    }
}
