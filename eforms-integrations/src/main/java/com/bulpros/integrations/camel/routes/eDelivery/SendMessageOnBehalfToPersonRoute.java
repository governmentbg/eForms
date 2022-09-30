package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.SendMessageOnBehalfToPersonRequest;
import com.bulpros.integrations.eDelivery.model.SendMessageOnBehalfToPersonResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessageOnBehalfToPersonRoute extends RouteBuilder {

    @Override/**/
    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");

        rest("/eDelivery/send-message-on-behalf-to-person")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SendMessageOnBehalfToPersonRequest.class)
                .outType(SendMessageOnBehalfToPersonResponse.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliverySendMessageOnBehalfToPerson")
                .to("bean:eDeliveryService?method=sendMessageOnBehalfToPerson(${body})");
    }

}
