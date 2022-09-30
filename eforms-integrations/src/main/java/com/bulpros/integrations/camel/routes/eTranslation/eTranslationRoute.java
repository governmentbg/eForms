package com.bulpros.integrations.camel.routes.eTranslation;

import com.bulpros.integrations.eTranslation.model.TranslationRequest;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class eTranslationRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/eTranslation/translate")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(TranslationRequest.class)
                .outType(String.class)
                .route().routeGroup("ETRANSLATION").routeId("ETranslationTranslate")
                .to("bean:eTranslationService?method=translate(${body})");
    }
}
