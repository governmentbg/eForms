package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.DcLegalPersonRegistrationInfo;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckLegalPersonHasRegistrationRoute extends RouteBuilder {

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
        rest("/eDelivery/check-legal-person-has-registration")
                .get()
                .param().name("identificator").type(RestParamType.query)
                .description("Legal person eik")
                .dataType("string").required(true).endParam()
                .outType(DcLegalPersonRegistrationInfo.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliveryCheckLegalPersonHasRegistration")
                .to("bean:eDeliveryService?method=checkLegalPersonHasRegistration(${header.identificator})");

    }
}
