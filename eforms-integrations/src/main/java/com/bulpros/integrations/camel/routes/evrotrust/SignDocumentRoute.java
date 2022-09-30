package com.bulpros.integrations.camel.routes.evrotrust;

import com.bulpros.integrations.evrotrust.model.SignDocumentsRequest;
import com.bulpros.integrations.evrotrust.model.EvrotrustSignDocumentResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignDocumentRoute extends RouteBuilder {

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
        rest("/evrotrust/document/sign")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(SignDocumentsRequest.class)
                .outType(EvrotrustSignDocumentResponse.class)
                .route().routeGroup("EVROTRUST").routeId("EvrotrustDocumentsSign")
                .to("bean:EvrotrustService?method=signDocuments(${body})");
    }
}
