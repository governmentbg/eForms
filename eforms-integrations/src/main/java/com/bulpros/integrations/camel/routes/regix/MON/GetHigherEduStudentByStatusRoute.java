package com.bulpros.integrations.camel.routes.regix.MON;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.MON.GetHigherEduStudentByStatus.HigherEduStudentByStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetHigherEduStudentByStatusRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/mon/get-higher-edu-student-by-status")
                .get()
                .param().name("student-identifier").type(RestParamType.query)
                .description("Student identifier")
                .dataType("string").required(true).endParam()
                .param().name("student-status").type(RestParamType.query)
                .description("Student status")
                .dataType("StudentStatusType").required(true).endParam()
                .outType(HigherEduStudentByStatusResponse.class)
                .route().routeGroup("REGIX").routeId("RegixMonGetHigherEduStudentByStatus")
                .to("bean:regixMONService?method=getHigherEduStudentByStatus(${header.student-identifier}, ${header.student-status})");

    }
}
