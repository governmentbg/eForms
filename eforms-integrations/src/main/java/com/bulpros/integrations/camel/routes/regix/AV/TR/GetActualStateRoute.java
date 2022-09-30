package com.bulpros.integrations.camel.routes.regix.AV.TR;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.AV.TR.GetActualState.ActualStateResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetActualStateRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/av/tr/get-actual-state")
                .get()
                .param().name("uic").type(RestParamType.query)
                .description("Company UIC number")
                .dataType("string").required(true).endParam()
                .outType(ActualStateResponseType.class)
                .route().routeGroup("REGIX").routeId("RegixGetActualState")
                .to("bean:regixAVTRService?method=getActualState(${header.uic})");

    }
}
