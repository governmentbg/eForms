package com.bulpros.integrations.camel.routes.borica;

import com.bulpros.integrations.borica.model.SignDocumentsRequest;
import com.bulpros.integrations.borica.model.BoricaSignDocumentsResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignDocumentsRoute extends RouteBuilder {

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
                .post()
                    .param().name("rpToClientAuthorization")
                    .type(RestParamType.query)
                    .dataType("string")
                    .required(true)
                .endParam()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SignDocumentsRequest.class)
                .outType(BoricaSignDocumentsResponse.class)
                .route().routeGroup("BORICA").routeId("BoricaSignDocument")
                .to("bean:BoricaService?method=signDocuments(${body}, ${header.rpToClientAuthorization})");
    }
}
