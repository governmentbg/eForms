package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.service.NotificationService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Slf4j
public class NotificationListener implements TaskListener {

    @Autowired
    protected ConfigurationProperties processConfProperties;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AuthenticationFacade authenticationFacade;
    private Expression formTemplateKey;
    private Expression recipientEmail;

    @Override
    public void notify(DelegateTask delegateTask) {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        var formKey = (String) this.getFormTemplateKey().getValue(delegateTask.getExecution());
        var email = (String) this.getRecipientEmail().getValue(delegateTask.getExecution());
        var expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        var projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                .getValue(delegateTask.getExecution());
        var variables = delegateTask.getExecution().getVariables();
        notificationService.sendMailNotification(formKey, projectId, email, variables, authentication);
    }
}
