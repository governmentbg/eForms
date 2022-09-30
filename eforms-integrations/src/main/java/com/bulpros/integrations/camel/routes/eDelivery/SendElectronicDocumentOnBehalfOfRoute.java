package com.bulpros.integrations.camel.routes.eDelivery;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;

import com.bulpros.integrations.eDelivery.model.SendElectronicDocumentOnBehalfOfRequest;
import com.bulpros.integrations.eDelivery.model.SendElectronicDocumentOnBehalfOfResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendElectronicDocumentOnBehalfOfRoute extends RouteBuilder {
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

        rest("/eDelivery/send-electronic-document-on-behalf-of")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SendElectronicDocumentOnBehalfOfRequest.class)
                .outType(SendElectronicDocumentOnBehalfOfResponse.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliverySendElectronicDocumentOnBehalf")
                .to("bean:eDeliveryService?method=sendElectronicDocumentOnBehalfOf(${body})");
    }
}
