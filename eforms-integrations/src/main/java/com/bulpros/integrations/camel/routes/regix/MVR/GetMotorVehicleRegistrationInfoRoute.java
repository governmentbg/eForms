package com.bulpros.integrations.camel.routes.regix.MVR;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.MVR.GetMotorVehicleRegistrationInfo.MotorVehicleRegistrationResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetMotorVehicleRegistrationInfoRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/mvr/get-motor-vehicle-registration-info")
                .get()
                .param().name("identifier").type(RestParamType.query)
                .description("Motor vehicle identifier")
                .dataType("string").required(true).endParam()
                .outType(MotorVehicleRegistrationResponseType.class)
                .route().routeGroup("REGIX").routeId("RegixMvrGetMotorVehicleRegistrationInfo")
                .to("bean:regixMVRService?method=getMotorVehicleRegistrationInfo(${header.identifier})");

    }
}
