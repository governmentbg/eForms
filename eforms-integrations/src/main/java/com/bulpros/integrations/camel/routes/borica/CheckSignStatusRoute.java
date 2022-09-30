package com.bulpros.integrations.camel.routes.borica;

import com.bulpros.integrations.borica.model.BoricaCheckSignStatusResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckSignStatusRoute extends RouteBuilder {

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
        rest("/borica/sign")
                .get("{callbackId}")
                .param()
                    .name("callbackId")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .outType(BoricaCheckSignStatusResponse.class)
                .route().routeGroup("BORICA").routeId("BoricaSign")
                .to("bean:BoricaService?method=checkSignStatus(${header.callbackId})");
        rest("/borica/rpcallbackid")
                .get("{rpCallbackId}")
                .param()
                    .name("rpCallbackId")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .outType(BoricaCheckSignStatusResponse.class)
                .route().routeGroup("BORICA").routeId("BoricaRpcallbackid")
                .to("bean:BoricaService?method=checkRelyingPartySignStatus(${header.rpCallbackId})");
    }
}
