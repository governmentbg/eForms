package com.bulpros.integrations.camel.routes.egov;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class GetEGovSupplierServicesRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/egov/get-egov-supplier-services")
                .get()
                .param().name("supplierEIK").type(RestParamType.query)
                .description("Supplier EIK")
                .dataType("string").required(true).endParam()
                .outType(Map.class)
                .route().routeGroup("EGOV").routeId("EGovGetSupplierService")
                .to("bean:eGovService?method=getEGovSupplierServices(${header.supplierEIK})");

    }
}
