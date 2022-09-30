package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.SendMessageRequest;
import com.bulpros.integrations.eDelivery.model.SendMessageResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessageRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");

        rest("/eDelivery/send-message")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SendMessageRequest.class)
                .outType(SendMessageResponse.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliverySendMessage")
                .to("bean:eDeliveryService?method=sendMessage(${body})");
    }

}
