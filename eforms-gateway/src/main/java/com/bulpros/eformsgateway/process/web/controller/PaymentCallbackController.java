package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.exception.InvalidPaymentStatusMessageException;
import com.bulpros.eformsgateway.process.repository.camunda.CamundaConfigurationProperties;
import com.bulpros.eformsgateway.process.service.PaymentStatusCallbackService;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatus;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusEnum;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusRequest;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/api/ePayment")
@Slf4j
public class PaymentCallbackController {

    @Autowired
    PaymentStatusCallbackService paymentStatusCallbackService;
    @Autowired
    CamundaConfigurationProperties camundaConfigurationProperties;

    @Timed(value = "eforms-gateway-payment-status-callback.time")
    @PostMapping(value = "/payment-status-callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentStatusResponseDto> paymentRequestCallback(
            @RequestParam("processId") String processId,
            @RequestParam ("message") String message,
            @RequestParam ("fieldId") String fieldId,
            PaymentStatusRequest paymentStatusRequest,
            ServerHttpRequest request) {

        if (request.getRemoteAddress() == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String remoteAddress = request.getRemoteAddress().getAddress().getCanonicalHostName();
        String[] allowedRemoteAddresses = camundaConfigurationProperties.getPaymentStatusCallbackRemoteAddresses().split("[ ,]+");
        if (remoteAddress == null ||
                (Arrays.stream(allowedRemoteAddresses).noneMatch(a -> a.equals(remoteAddress)) &&
                 Arrays.stream(allowedRemoteAddresses).noneMatch(a -> a.equals("*")))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        log.info("ePayment clientId = " + paymentStatusRequest.getClientId());
        log.info("ePayment hmac = " + paymentStatusRequest.getHmac());
        log.info("ePayment data = " + paymentStatusRequest.getData());

        if (paymentStatusRequest.getClientId() == null ||
                ! paymentStatusRequest.getClientId().equals(camundaConfigurationProperties.getPaymentStatusClientId())) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        if (paymentStatusRequest.getHmac() == null ||
                ! paymentStatusRequest.getHmac().equals(calculateHmac(paymentStatusRequest.getData()))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        byte[] data = Base64.getDecoder().decode(paymentStatusRequest.getData());
        PaymentStatus paymentStatus = null;
        try {
            paymentStatus = new ObjectMapper().readValue(data, PaymentStatus.class);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        PaymentStatusEnum status = PaymentStatusEnum.findByName(paymentStatus.getStatus());
        if (status == null) {
            throw new InvalidPaymentStatusMessageException(InvalidPaymentStatusMessageException.INVALID_PAYMENT_STATUS);
        }

        Date changeTime = null;
        try {
            if (paymentStatus.getChangeTime().length() == 27) {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                changeTime = sdf.parse(paymentStatus.getChangeTime().substring(0, 23));
            } else {
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                changeTime = sdf.parse(paymentStatus.getChangeTime().substring(0, 23) +
                        paymentStatus.getChangeTime().substring(27));
            }
        } catch (ParseException e) {
            throw new InvalidPaymentStatusMessageException(InvalidPaymentStatusMessageException.INVALID_PAYMENT_CHANGE_TIME_FORMAT);
        }

        return ResponseEntity.ok(paymentStatusCallbackService.
                paymentRequestCallback(processId, message, fieldId, paymentStatus.getId(), status, changeTime));
    }

    private String calculateHmac(String data) {
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        SecretKeySpec secret_key = new SecretKeySpec(camundaConfigurationProperties.getPaymentStatusSecretKey().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        try {
            hmacSha256.init(secret_key);
        } catch (InvalidKeyException e) {
            return null;
        }
        return Base64.getEncoder().encodeToString(hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

}
