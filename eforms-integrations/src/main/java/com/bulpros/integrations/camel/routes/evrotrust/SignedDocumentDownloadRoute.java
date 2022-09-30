package com.bulpros.integrations.camel.routes.evrotrust;

import com.bulpros.integrations.evrotrust.model.SignedDocumentDownloadResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignedDocumentDownloadRoute extends RouteBuilder {

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
        rest("/evrotrust/document/download")
                .get("/{transactionId}/{groupSigning}")
                .param()
                    .name("transactionId")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .param()
                    .name("groupSigning")
                    .type(RestParamType.path)
                    .dataType("boolean")
                .required(true)
                .endParam()
                .outType(SignedDocumentDownloadResponse[].class)
                .route().routeGroup("EVROTRUST").routeId("EvrotrustDocumentsDownload")
                .to("bean:EvrotrustService?method=getSignedDocuments(${header.transactionId}, ${header.groupSigning})");
    }
}
