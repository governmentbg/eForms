package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.NotificationMailRequest;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.FormsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Value("${com.bulpros.eforms-integrations.url}")
    private String integrationsUrl;
    @Value("${com.bulpros.eforms-integrations.notification.prefix}")
    private String notificationPrefix;

    private final TemplateEngine templateEngine;
    private final RestTemplate restTemplate;
    private final FormsService formService;
    protected final ConfigurationProperties processConfProperties;

    @Override
    public void sendMailNotification(String formKey, String projectId, String email, Map<String, Object> variables, Authentication authentication) {
        var formApiPath = EFormsUtils.getFormApiPath(formKey);
        var components = ((ArrayList<?>) formService.getForm(new ResourcePath(projectId, formApiPath), authentication).get("components"));
        var subjectComponent = components.stream()
                .filter(component ->
                        ((HashMap<?, ?>) component).containsKey("key") && ((HashMap<?, ?>) component).get("key").equals(processConfProperties.getNotificationSubject()))
                .findAny()
                .orElse(null);
        String subject = subjectComponent != null ? (String) ((HashMap<?, ?>) subjectComponent).get("html") : "";

        var bodyComponent = components.stream()
                .filter(component ->
                        ((HashMap<?, ?>) component).containsKey("key") && ((HashMap<?, ?>) component).get("key").equals(processConfProperties.getNotificationBody()))
                .findAny()
                .orElse(null);
        var body = bodyComponent != null ? (String) ((HashMap<?, ?>) bodyComponent).get("html") : "";
        final org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
        context.setVariables(variables);
        final String mailContent = templateEngine.process(body, context);
        final String subjectContent = templateEngine.process(subject, context);
        var requestBody = new NotificationMailRequest();
        requestBody.setTo(email);
        requestBody.setSubject(subjectContent);
        requestBody.setBody(mailContent);
        var requestEntity = new HttpEntity<>(requestBody);
        restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(this.integrationsUrl + this.notificationPrefix)
                .path("/post-mail-notification").toUriString(), HttpMethod.POST, requestEntity, String.class);
    }
}
