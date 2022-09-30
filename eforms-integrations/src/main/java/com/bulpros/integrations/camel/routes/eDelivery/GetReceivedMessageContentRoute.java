package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.DcMessageDetails;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetReceivedMessageContentRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/eDelivery/get-received-message-content")
                .get()
                .param()
                    .name("messageId")
                    .type(RestParamType.query)
                    .dataType("string")
                    .required(true)
                .endParam()
                .param()
                    .name("operatorEGN")
                    .type(RestParamType.query)
                    .dataType("string")
                    .required(true)
                    .endParam()
                .outType(DcMessageDetails.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliveryGetReceivedMessageContent")
                .to("bean:eDeliveryService?method=getReceivedMessageContentResponse(${header.messageId}, ${header.operatorEGN})");
    }
}
