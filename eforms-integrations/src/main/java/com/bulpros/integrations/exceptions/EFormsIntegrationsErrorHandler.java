package com.bulpros.integrations.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.*;

import javax.xml.ws.soap.SOAPFaultException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class EFormsIntegrationsErrorHandler {

    @Handler
    public void prepareErrorResponse(Exchange exchange) {
        String routeId = exchange.getFromRouteId();
        String routeGroup = exchange.getContext().getRoute(routeId).getGroup();
        Throwable throwable = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        log.error(throwable.getMessage(), throwable);
        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        ExceptionBody exceptionBody;
        if (throwable instanceof RestClientResponseException) {
            HttpStatusCodeException exception = ((HttpStatusCodeException) throwable);
            exceptionBody = ExceptionBody.builder()
                    .status(exception.getStatusCode().value())
                    .error(exception.getStatusCode().getReasonPhrase())
                    .message(prepareExceptionMessageCode(routeGroup, IntegrationsExCauseType.COMMUNICATION))
                    .data(exception.getResponseBodyAsString()).build();

        } else if (throwable instanceof RestClientException) {
            RestClientException exception = ((RestClientException) throwable);
            exceptionBody = ExceptionBody.builder()
                    .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                    .message(prepareExceptionMessageCode(routeGroup, IntegrationsExCauseType.UNAVAILABLE))
                    .data(exception.getMessage()).build();
        } else if (throwable instanceof SOAPFaultException) {
            SOAPFaultException exception = ((SOAPFaultException) throwable);
            exceptionBody = ExceptionBody.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error(exception.getMessage())
                    .message(prepareExceptionMessageCode(routeGroup, IntegrationsExCauseType.COMMUNICATION))
                    .data(exception.getMessage()).build();
        } else if (throwable instanceof UnknownHostException) {
            UnknownHostException exception = ((UnknownHostException) throwable);
            exceptionBody = ExceptionBody.builder()
                    .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
                    .message(prepareExceptionMessageCode(routeGroup, IntegrationsExCauseType.UNAVAILABLE))
                    .data(exception.getMessage()).build();
        } else {
            exceptionBody = ExceptionBody.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message(prepareExceptionMessageCode(routeGroup, null))
                    .data(throwable.getMessage()).build();
        }

        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, "500");
        msg.setBody(exceptionBody);
    }

    private String prepareExceptionMessageCode(String externalSystem, IntegrationsExCauseType exCauseType) {
        return Stream.of("ERROR", "INTEGRATIONS", externalSystem, exCauseType)
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining("."));
    }
}
