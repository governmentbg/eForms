package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.epayment.model.PaymentStatusCallbackRequest;
import com.bulpros.eforms.processengine.epayment.model.PaymentStatusResponse;
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
@RequestMapping({ "/eforms-rest/payment" })
@AllArgsConstructor
public class PaymentCallbackController {

    private final RuntimeService runtimeService;

    @PostMapping(value = "/payment-status-callback", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PaymentStatusResponse> setPaymentStatus(@RequestBody PaymentStatusCallbackRequest paymentStatus) {
        PaymentStatusResponse response;
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
                response = new PaymentStatusResponse(true);
            } else {
                response = new PaymentStatusResponse(false);
            }
        } catch (Exception e) {
            response = new PaymentStatusResponse(false);
            System.out.println(e.getMessage());
            // Query return ??? results instead of max 1
        }

        return ResponseEntity.ok(response);
    }
}
