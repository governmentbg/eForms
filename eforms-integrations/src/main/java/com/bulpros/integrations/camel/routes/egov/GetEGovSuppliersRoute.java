package com.bulpros.integrations.camel.routes.egov;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class GetEGovSuppliersRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/egov/get-egov-suppliers")
                .get()
                .outType(Map.class)
                .route().routeGroup("EGOV").routeId("EGovGetSuppliers")
                .to("bean:eGovService?method=getEGovSuppliers()");

    }
}
