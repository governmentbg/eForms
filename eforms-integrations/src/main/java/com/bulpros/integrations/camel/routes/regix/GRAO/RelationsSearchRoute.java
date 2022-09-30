package com.bulpros.integrations.camel.routes.regix.GRAO;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.GRAO.RelationsSearch.RelationsResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RelationsSearchRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/grao/relations-search")
                .get()
                .param().name("egn").type(RestParamType.query)
                .description("Physical person EGN number")
                .dataType("string").required(true).endParam()
                .outType(RelationsResponseType.class)
                .route().routeGroup("REGIX").routeId("RegixGraoRelationsSearch")
                .to("bean:regixGraoService?method=relationsSearch(${header.egn})");

    }
}
