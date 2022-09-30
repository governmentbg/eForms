package com.bulpros.integrations.camel.routes.ePayment;

import com.bulpros.integrations.ePayment.model.*;
import com.bulpros.integrations.exceptions.EFormsIntegrationsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentRequestRoute extends RouteBuilder {

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

        rest("/ePayment/register-payment-extended")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(PaymentRequest.class)
                .outType(RegisterPaymentResponse.class)
                .route().routeGroup("EPAYMENT").routeId("EPaymentRegisterPaymentExtended")
                .to("bean:ePaymentService?method=registerPaymentExtended(${body})");

        rest("/ePayment/payment-status")
                .get()
                .param().name("paymentId").type(RestParamType.query)
                .dataType("string").required(true).endParam()
                .outType(PaymentStatusResponse.class)
                .route().routeGroup("EPAYMENT").routeId("EPaymentPaymentStatus")
                .to("bean:ePaymentService?method=paymentStatus(${header.paymentId})");

        rest("/ePayment/change-payment-status")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(ChangePaymentStatusRequest.class)
                .outType(RegisterPaymentResponse.class)
                .route().routeGroup("EPAYMENT").routeId("EPaymentChangePaymentStatus")
                .to("bean:ePaymentService?method=changePaymentStatus(${body})");

        rest("/ePayment/vpos/pay-with-borica")
                .post()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(PayWithBoricaRequest.class)
                .outType(PayWithBoricaResponse.class)
                .route().routeId("EPaymentPayWithBorica")
                .to("bean:ePaymentService?method=payWithBorica(${body})");
    }
}
