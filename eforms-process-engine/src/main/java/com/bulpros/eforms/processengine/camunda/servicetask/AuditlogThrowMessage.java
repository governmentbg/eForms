package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.EventTypeEnum;
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
@Named("AuditlogThrowMessage")
@Slf4j
@RequiredArgsConstructor
public class AuditlogThrowMessage implements JavaDelegate {

    private final AuditlogRepository аuditlogEventRepository;

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
                аuditlogEventRepository.registerEvent(eventType, eventDescription, delegateExecution);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
