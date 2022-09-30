package com.bulpros.integrations.camel.routes.regix.GRAO;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.GRAO.TemporaryAddressSearch.TemporaryAddressResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TemporaryAddressSearchRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/grao/temporary-address-search")
                .get()
                .param().name("egn").type(RestParamType.query)
                .description("Physical person EGN number")
                .dataType("string").required(true).endParam()
                .param().name("search-date").type(RestParamType.query)
                .dataType("string").dataFormat("date").required(true).endParam()
                .outType(TemporaryAddressResponseType.class)
                .route().routeGroup("REGIX").routeId("RegixGraoTemporaryAddressSearch")
                .to("bean:regixGraoService?method=temporaryAddressSearch(${header.egn},${header.search-date})");

    }
}
