package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.auditlog.service.AuditlogService;
import com.bulpros.eforms.processengine.camunda.repository.AuditlogRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Setter
@Getter
@Named("AuditlogRegisterEvent")
@Slf4j
@RequiredArgsConstructor
public class AuditlogRegisterEvent implements JavaDelegate {

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
            }
        }
    }
}
