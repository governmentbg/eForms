package com.bulpros.integrations.camel.routes.evrotrust;

import com.bulpros.integrations.evrotrust.model.UserCheckExtendedRequest;
import com.bulpros.integrations.evrotrust.model.UserCheckExtendedResponse;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCheckExtendedRoute extends RouteBuilder {

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

        rest("/evrotrust/user/check")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(UserCheckExtendedRequest.class)
                .outType(UserCheckExtendedResponse.class)
                .route().routeId("EvrotrustPostUserCheck")
                .to("bean:EvrotrustService?method=userCheckExtendedPost(${body})");

        rest("/evrotrust/user/check")
                .get("/{identifier}")
                    .param()
                    .name("identifier")
                    .type(RestParamType.path)
                    .dataType("string")
                    .required(true)
                .endParam()
                .outType(UserCheckExtendedResponse.class)
                .route().routeGroup("EVROTRUST").routeId("EvrotrustGetDocumentSign")
                .to("bean:EvrotrustService?method=userCheckExtendedGet(${header.identifier})");
    }
}
