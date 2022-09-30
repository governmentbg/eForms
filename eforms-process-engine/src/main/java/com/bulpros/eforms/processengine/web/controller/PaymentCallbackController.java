package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.epayment.model.PaymentCallbackStatusResponse;
import com.bulpros.eforms.processengine.epayment.model.PaymentStatusCallbackRequest;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/eforms-rest/payment"})
@AllArgsConstructor
public class PaymentCallbackController {

    private final RuntimeService runtimeService;

    @Timed(value = "eforms-process-engine-set-payment-status.time")
    @PostMapping(value = "/payment-status-callback", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentCallbackStatusResponse> setPaymentStatus(@RequestBody PaymentStatusCallbackRequest paymentStatus) {
        PaymentCallbackStatusResponse response;
        try {
            Execution execution = runtimeService.createExecutionQuery()
                    .messageEventSubscriptionName(paymentStatus.getMessage())
                    .processInstanceId(paymentStatus.getProcessId())
                    .processVariableValueEquals(paymentStatus.getFieldId(), paymentStatus.getId()).singleResult();

            if (execution != null) {
                Map<String, Object> variables = new HashMap<>();
                variables.put(paymentStatus.getFieldId(), paymentStatus.getId());
                variables.put("ePaymentStatus", paymentStatus.getStatus());
                variables.put("ePaymentStatusChangeTime",
                        paymentStatus.getChangeTime() != null ? paymentStatus.getChangeTime() : Calendar.getInstance().getTime());
                runtimeService.messageEventReceived(paymentStatus.getMessage(), execution.getId(), variables);
                response = new PaymentCallbackStatusResponse(true);
            } else {
                response = new PaymentCallbackStatusResponse(false);
            }
        } catch (Exception e) {
            response = new PaymentCallbackStatusResponse(false);
            System.out.println(e.getMessage());
            // Query return ??? results instead of max 1
        }

        return ResponseEntity.ok(response);
    }
}
