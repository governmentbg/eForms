package com.bulpros.integrations.camel.routes.eDelivery;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.bulpros.integrations.eDelivery.model.SendMessageOnBehalfOfRequest;
import com.bulpros.integrations.eDelivery.model.SendMessageOnBehalfOfResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendMessageOnBehalfOfRoute extends RouteBuilder {

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

        rest("/eDelivery/send-message-on-behalf-of")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SendMessageOnBehalfOfRequest.class)
                .outType(SendMessageOnBehalfOfResponse.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliverySendMessageOnBehalf")
                .to("seda:eDeliveryQueue?concurrentConsumers=3&timeout=0");
        from("seda:eDeliveryQueue?concurrentConsumers=3&timeout=0")
                .to("bean:eDeliveryService?method=sendMessageOnBehalfOf(${body})");
    }

}
