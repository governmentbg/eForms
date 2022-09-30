package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.eforms.processengine.camunda.repository.AuditlogRepository;
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
@Named("AuditlogThrowMessage")
public class AuditlogThrowMessageDelegate implements JavaDelegate {

    private final AuditlogRepository auditlogEventRepository;

    private Expression eventType;
    private Expression eventDescription;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            EventTypeEnum eventType = this.getEventType() != null ?
                    EventTypeEnum.valueOf((String) this.getEventType().getValue(delegateExecution)) : null;
            String eventDescription = this.getEventDescription() != null ?
                    (String) this.getEventDescription().getValue(delegateExecution) : null;

            if (eventType != null) {
                auditlogEventRepository.registerEvent(eventType, eventDescription, delegateExecution);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "AUDITLOG.UNAVAILABLE", exception.getMessage());
        }
    }
}
