package com.bulpros.integrations.camel.routes.borica;

import com.bulpros.integrations.borica.model.BoricaCheckCertificateResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckCertificateRoute extends RouteBuilder {

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
        rest("/borica/cert/identity")
                .get("/{identificatorType}/{identityValue}")
                .param()
                    .name("identificatorType")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .param()
                    .name("identityValue")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                    .endParam()
                .outType(BoricaCheckCertificateResponse.class)
                .route().routeGroup("BORICA").routeId("BoricaCertIdentity")
                .to("bean:BoricaService?method=checkCertificate(${header.identificatorType}, ${header.identityValue})");
    }
}
