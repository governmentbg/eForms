package com.bulpros.integrations.camel.routes.orn;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.orn.model.UniqueNumberResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetUniqueNumberRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/orn/get-unique-number")
                .get()
                .param().name("bulstat").type(RestParamType.query)
                .description("If the applicant is a person, the PIN is submitted\n" +
                        "If the applicant is a legal entity, it is submitted to the UIC\n" +
                        "If the applicant is an administration, the UIC is submitted")
                .dataType("string").required(true).endParam()

                .param().name("typeService").type(RestParamType.query)
                .description("The unique identifier of the service, which was received by EPDEAU, is submitted")
                .dataType("string").required(true).endParam()

                .param().name("uriService").type(RestParamType.query)
                .description("e-service is always provided")
                .dataType("string").required(true).endParam()
                .outType(UniqueNumberResponse.class)
                .route().routeGroup("ORN").routeId("OrnGetUniqueNumber")
                .to("seda:ornQueue");
        from("seda:ornQueue")
                .to("bean:ornService?method=getUniqueNumber(${header.bulstat},${header.typeService},${header.uriService})");


    }
}
