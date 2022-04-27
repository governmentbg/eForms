package com.bulpros.eforms.processengine.camunda.sendtask;


import com.bulpros.eforms.processengine.camunda.model.UserProfileDto;
import com.bulpros.eforms.processengine.camunda.service.NotificationService;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;

import javax.inject.Named;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@Named("sendMail")
@RequiredArgsConstructor
public class SendMailDelegate implements JavaDelegate {

    private final ConfigurationProperties processConfProperties;

    private final NotificationService notificationService;

    private final AuthenticationFacade authenticationFacade;

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private Expression formTemplateKey;
    private Expression recipientEmail;

    private static final String DELIMETER = ",";

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Authentication authentication = AuthenticationService.createServiceAuthentication();
        var formKey = (String) this.getFormTemplateKey().getValue(execution);
        var recipient = this.getRecipientEmail().getValue(execution);
        try {
            recipient = objectMapper.readValue(Objects.toString(recipient), new TypeReference<>() {});
        } catch (IOException e) {
            log.debug("Recipient is not of type JSON");
        }
        var email = evaluateEmail(recipient);
        var expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        var projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                .getValue(execution);
        var variables = execution.getVariables();
        Arrays.stream(email.split(DELIMETER)).forEach(e -> {
            if (!e.isBlank()) {
                notificationService.sendMailNotification(formKey, projectId, e, variables, authentication);
            } else {                
                log.warn(String.format("Skipping execution of NotificationService with no recipient. Business key: %s, Activity: %s", 
                        execution.getProcessBusinessKey(), execution.getCurrentActivityName()));
            }
        });
    }

    private String evaluateEmail(Object recipient) {
        if (recipient instanceof String) {
            return Objects.toString(recipient);
        } else if (recipient instanceof Collection) {
            return ((Collection<?>) recipient).stream().map(r -> evaluateEmail(r)).collect(Collectors.joining(DELIMETER));
        } else if (recipient instanceof Object) {
            return modelMapper.map(recipient, UserProfileDto.class).getEmail();
        }
        return "";
    }
}
