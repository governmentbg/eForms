package com.bulpros.integrations.camel.routes.borica;

import com.bulpros.integrations.borica.model.SignedDocumentResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetSignedDocumentRoute extends RouteBuilder {

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
        rest("/borica/sign/content")
                .get("{signature}")
                .param()
                    .name("signature")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .outType(SignedDocumentResponse.class)
                .route().routeGroup("BORICA").routeId("BoricaSignContent")
                .to("bean:BoricaService?method=getSignedDocument(${header.signature})");
    }
}
