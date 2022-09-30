package com.bulpros.integrations.camel.routes.auditlog;

import com.bulpros.integrations.auditlog.model.AuditlogRequest;
import com.bulpros.integrations.auditlog.model.AuditlogResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@Slf4j
public class AuditlogRoute extends RouteBuilder {

    @Override
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");

        rest("/auditlog/register-event")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(AuditlogRequest.class)
                .outType(AuditlogResponse.class)
                .route().routeGroup("AUDITLOG").routeId("AuditlogRegisterEvent")
                .to("direct:asyncExecution");

        from("direct:asyncExecution")
                .process(exchange -> {
                    CamelContext context = exchange.getContext();
                    ProducerTemplate producerTemplate = context.createProducerTemplate();
                    AuditlogRequest body = exchange.getIn().getBody(AuditlogRequest.class);
                    Future<String> registerEvent = producerTemplate.asyncRequestBody
                            ("bean:auditlogService?method=registerEvent(${body})", body, String.class);
                    exchange.getMessage()
                            .setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
                    exchange.getMessage().setBody(null);
                });

    }
}
