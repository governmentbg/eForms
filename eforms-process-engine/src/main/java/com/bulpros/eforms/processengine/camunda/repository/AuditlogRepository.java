package com.bulpros.eforms.processengine.camunda.repository;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.EventTypeEnum;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public interface AuditlogRepository {

    void registerEvent(EventTypeEnum eventType, String eventDescription, DelegateExecution delegateExecution);
    AuditlogRequest createEvent(EventTypeEnum eventType, String eventDescription, DelegateExecution delegateExecution);

}
