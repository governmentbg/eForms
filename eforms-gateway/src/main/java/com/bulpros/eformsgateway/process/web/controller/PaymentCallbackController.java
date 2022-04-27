package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.process.repository.camunda.CamundaConfigurationProperties;
import com.bulpros.eformsgateway.process.service.PaymentStatusCallbackService;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatus;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/ePayment")
public class PaymentCallbackController {

    @Autowired
    PaymentStatusCallbackService paymentStatusCallbackService;
    @Autowired
    CamundaConfigurationProperties camundaConfigurationProperties;

    @PostMapping("/payment-status-callback")
    public ResponseEntity<PaymentStatusResponseDto> paymentRequestCallback(
            @RequestParam ("processId") String processId,
            @RequestParam ("message") String message,
            @RequestParam ("fieldId") String fieldId,
            @RequestBody PaymentStatus paymentStatus,
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

        return ResponseEntity.ok(paymentStatusCallbackService.paymentRequestCallback(processId, message, fieldId, paymentStatus));
    }
}
