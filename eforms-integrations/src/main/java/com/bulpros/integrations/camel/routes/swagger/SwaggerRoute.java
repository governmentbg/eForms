//package com.bulpros.integrations.camel.routes.swagger;
//
//import org.apache.camel.builder.RouteBuilder;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerRoute  extends RouteBuilder {
//    @Override
//    public void configure() throws Exception {
//        restConfiguration()
//                .component("servlet")
//                .apiContextPath("/swagger")
//                .apiContextRouteId("swagger")
//
//                .contextPath("/integrations")
//                .apiProperty("api.title", "EForms integration layer")
//                .apiProperty("api.version", "1.1.0")
//                .apiProperty("api.contact.name", "Digitall Ltd.")
//                .apiProperty("api.contact.url", "https://digitall.com/");
//    }
//}
