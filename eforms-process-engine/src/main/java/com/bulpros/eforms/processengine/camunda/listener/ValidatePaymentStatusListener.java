package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestStatusRequest;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestStatusResponse;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestsById;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Map;

@Getter
@Setter
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidatePaymentStatusListener implements TaskListener {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    private final RestTemplate restTemplate;

    private Expression eServiceClientId;
    private Expression ePaymentId;
    private Expression ePaymentStatus;

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String,Object> processContext = (Map<String, Object>) delegateTask.getVariable(ProcessConstants.CONTEXT);
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String hasFixedPayment = JsonPath.using(pathConfiguration).parse(processContext)
                .read("$.serviceSupplier.data.hasFixedPayment");
        if (!"beforeDelivery".equals(hasFixedPayment)) {
            return;
        }

        String eServiceClientId = (String) this.getEServiceClientId().getValue(delegateTask);
        String ePaymentId = (String) this.getEPaymentId().getValue(delegateTask);

        PaymentRequestStatusRequest statusRequest = new PaymentRequestStatusRequest();
        statusRequest.setEServiceClientId(eServiceClientId);
        PaymentRequestsById requestIds = new PaymentRequestsById();
        requestIds.setRequestIds(Arrays.asList(ePaymentId));
        statusRequest.setRequestIds(requestIds);
        try {
            PaymentRequestStatusResponse response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-request-status").toUriString(),
                    statusRequest, PaymentRequestStatusResponse.class);
            if (response.getPaymentStatuses().size() > 0) {
                String paymentStatus = response.getPaymentStatuses().get(0).getStatus();
                delegateTask.setVariable("ePaymentStatus", paymentStatus);
                if ("pending".equals(paymentStatus)) {
                    throw new EFormsProcessEngineException("PAYMENT_IS_STILL_PENDING");
                }
            }
        } catch (RestClientException e) {
            delegateTask.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }
}
