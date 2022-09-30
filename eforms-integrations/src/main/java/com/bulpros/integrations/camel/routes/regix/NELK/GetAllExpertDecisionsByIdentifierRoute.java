package com.bulpros.integrations.camel.routes.regix.NELK;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.NELK.GetAllExpertDecisionsByIdentifier.ExpertDecisionsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetAllExpertDecisionsByIdentifierRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/nelk/get-all-expert-decisions-by-identifier")
                .get()
                .param().name("identifier").type(RestParamType.query)
                .description("Person identifier")
                .dataType("string").required(true).endParam()
                .outType(ExpertDecisionsResponse.class)
                .route().routeGroup("REGIX").routeId("RegixNelkGetAllExpertDecisionsByIdentifier")
                .to("bean:regixNELKService?method=getAllExpertDecisionsByIdentifier(${header.identifier})");

    }
}
