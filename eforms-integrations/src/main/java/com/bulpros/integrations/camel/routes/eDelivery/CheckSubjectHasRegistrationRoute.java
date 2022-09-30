package com.bulpros.integrations.camel.routes.eDelivery;

import com.bulpros.integrations.eDelivery.model.DcSubjectRegistrationInfo;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckSubjectHasRegistrationRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/eDelivery/check-subject-has-registration")
                .get()
                .param().name("identifier").type(RestParamType.query)
                .description("Physical person EGN/LNCH/EIK number")
                .dataType("string").required(true).endParam()
                .outType(DcSubjectRegistrationInfo.class)
                .route().routeGroup("EDELIVERY").routeId("EDeliveryCheckSubjectHasRegistration")
                .to("bean:eDeliveryService?method=checkSubjectHasRegistration(${header.identifier})");

    }
}
