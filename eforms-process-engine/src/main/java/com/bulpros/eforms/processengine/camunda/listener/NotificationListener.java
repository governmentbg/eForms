package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.service.NotificationService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.context.Context;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener implements TaskListener {

    protected final ConfigurationProperties processConfProperties;
    private final NotificationService notificationService;
    private final AuthenticationFacade authenticationFacade;
    private Expression formTemplateKey;
    private Expression recipientEmail;

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            Authentication authentication = this.authenticationFacade.getAuthentication();
            var formKey = (String) this.getFormTemplateKey().getValue(delegateTask.getExecution());
            var email = (String) this.getRecipientEmail().getValue(delegateTask.getExecution());
            var expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
            var projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                    .getValue(delegateTask.getExecution());
            var variables = delegateTask.getExecution().getVariables();
            notificationService.sendMailNotification(formKey, projectId, email, variables, authentication);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "NOTIFICATION", e.getMessage());
        }
    }
}
