package com.bulpros.integrations.camel.routes.eDelivery;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import com.bulpros.integrations.eDelivery.model.DcPersonRegistrationInfo;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CheckPersonHasRegistrationRoute extends RouteBuilder {

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
        rest("/eDelivery/check-person-has-registration")
                .get()
                .param().name("person-identificator").type(RestParamType.query)
                .description("Physical person EGN/LNCH number")
                .dataType("string").required(true).endParam()
                .outType(DcPersonRegistrationInfo.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliveryCheckPersonHasRegistration")
                .to("bean:eDeliveryService?method=checkPersonHasRegistration(${header.person-identificator})");

    }
}
