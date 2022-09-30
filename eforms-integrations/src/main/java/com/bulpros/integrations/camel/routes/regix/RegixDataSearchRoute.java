package com.bulpros.integrations.camel.routes.regix;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.RegixResponseData;
import com.bulpros.integrations.regix.model.RegixSearchData;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegixDataSearchRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/search")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(RegixSearchData.class)
                .outType(RegixResponseData.class)
                .route().routeGroup("REGIX").routeId("RegixSearch")
                .to("bean:regixService?method=search(${body})");
    }
}
