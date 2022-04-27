package com.bulpros.eforms.processengine.camunda.servicetask;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import javax.inject.Named;

@Named("AuditlogThrowEndMessage")
@Slf4j
public class AuditlogThrowEndMessage implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            delegateExecution.setVariable("stopAuditlog", true);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
