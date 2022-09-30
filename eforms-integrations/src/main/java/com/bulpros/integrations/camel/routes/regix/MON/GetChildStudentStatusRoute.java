package com.bulpros.integrations.camel.routes.regix.MON;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.MON.GetChildStudentStatus.ChildStudentStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetChildStudentStatusRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/mon/get-child-student-status")
                .get()
                .param().name("child-identifier").type(RestParamType.query)
                .description("Child id")
                .dataType("string").required(true).endParam()
                .outType(ChildStudentStatusResponse.class)
                .route().routeGroup("REGIX").routeId("RegixMonGetChildStudentStatus")
                .to("bean:regixMONService?method=getChildStudentStatus(${header.child-identifier})");

    }
}
