package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.DcPartialListOfDcMessageHR29GRRX;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetReceivedMessagesListPagedRoute extends RouteBuilder {
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
        rest("/eDelivery/get-received-messages")
                .get()
                .param()
                    .name("onlyNew")
                    .type(RestParamType.query)
                    .dataType("boolean")
                    .required(true)
                    .endParam()
                .param()
                    .name("page")
                    .type(RestParamType.query)
                    .dataType("int")
                    .required(true)
                    .endParam()
                .param()
                    .name("size")
                    .type(RestParamType.query)
                    .dataType("int")
                    .required(true)
                    .endParam()
                .param()
                    .name("operatorEGN")
                    .type(RestParamType.query)
                    .dataType("string")
                    .required(false)
                    .endParam()
                .outType(DcPartialListOfDcMessageHR29GRRX.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliveryGetReceivedMessage")
                .to("bean:eDeliveryService?method=getReceivedMessagesListPagedResponse(${header.onlyNew}, ${header.page}, ${header.size}, ${header.operatorEGN})");
    }
}
