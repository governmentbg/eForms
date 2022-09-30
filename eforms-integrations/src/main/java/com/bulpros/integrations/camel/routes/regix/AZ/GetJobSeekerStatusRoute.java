package com.bulpros.integrations.camel.routes.regix.AZ;

import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import com.bulpros.integrations.regix.model.AZ.GetJobSeekerStatus.JobSeekerStatusData;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetJobSeekerStatusRoute extends RouteBuilder {

    public void configure() {
        onException(Exception.class)
                .handled(true)
                .bean(EFormsIntegrationsErrorHandler.class);

        restConfiguration().component("servlet")
                .clientRequestValidation(true)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Origin", "*");
        rest("/regix/az/get-job-seeker-status")
                .get()
                .param().name("personal-id").type(RestParamType.query)
                .description("Physical person personal id")
                .dataType("string").required(true).endParam()
                .outType(JobSeekerStatusData.class)
                .route().routeGroup("REGIX").routeId("RegixGetJobSeekerStatus")
                .to("bean:regixAZService?method=getJobSeekerStatus(${header.personal-id})");

    }
}
