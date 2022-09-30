package com.bulpros.integrations.camel.routes.notification;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.notification.model.NotificationMailRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostSendMailNotification extends RouteBuilder {
    final SmtpProperties properties;
    
    @Override
    public void configure() throws Exception {

        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration()
                .component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/notifications/post-mail-notification")
                .post()
                .type(NotificationMailRequest.class)
                .route().routeGroup("MAIL").routeId("NotificationsPostMail")
                .to("direct:send-mail");
        final Map<String, String> smtp = properties.getSmtp();
        final StringBuilder params = new StringBuilder();
        for (String key : smtp.keySet()) {
            params.append("&mail.smtp." + key + "=" + smtp.get(key));
        }
        from("direct:send-mail")
                .inputType(NotificationMailRequest.class)
                .setHeader("subject", simple("${body.subject}"))
                .setHeader("to", simple("${body.to}"))
                .setHeader("Content-Type", constant("text/html"))
                .setBody(simple("${body.body}"))
                .to(String.format("smtp://%s:%s?username=%s&password=%s%s", 
                        properties.getHost(), properties.getPort(), 
                        properties.getUsername(), properties.getPassword(),
                        params));

    }
}

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "mail")
class SmtpProperties {
    private String host;
    private String port;
    private String username;
    private String password;
    private Map<String, String> smtp;
}
