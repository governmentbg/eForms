package com.bulpros.integrations.camel.routes.regix.MVR;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.MVR.GetPersonalIdentityV3.PersonalIdentityInfoResponseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetPersonalIdentityV3Route extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/mvr/get-personal-identity-v3")
                .get()
                .param().name("egn").type(RestParamType.query)
                .description("Physical person EGN number")
                .dataType("string").required(true).endParam()
                .param().name("identity-document-number").type(RestParamType.query)
                .description("Physical person identity card number")
                .dataType("string").required(true).endParam()
                .outType(PersonalIdentityInfoResponseType.class)
                .route().routeGroup("REGIX").routeId("RegixMvrGetPersonalInfoV3")
                .to("bean:regixMVRService?method=getPersonalIdentityV3(${header.egn},${header.identity-document-number})");
    }
}
