package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.epayment.model.PaymentStatusResponse;
import com.bulpros.eforms.processengine.epayment.model.enums.PaymentStatusType;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
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
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final Configuration jsonPathConfiguration;

    private Expression eServiceClientId;
    private Expression ePaymentId;
    private Expression ePaymentStatus;

    @Override
    public void notify(DelegateTask delegateTask) {
        Map<String, Object> processContext = (Map<String, Object>) delegateTask.getVariable(ProcessConstants.CONTEXT);
        String hasFixedPayment = JsonPath.using(jsonPathConfiguration).parse(processContext)
                .read("$.serviceSupplier.data.hasFixedPayment");
        if (!"beforeDelivery".equals(hasFixedPayment)) {
            return;
        }

        String ePaymentId = (String) this.getEPaymentId().getValue(delegateTask);

        try {
            PaymentStatusResponse response = restTemplate.getForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-status").queryParam("paymentId", ePaymentId).toUriString(),
                    PaymentStatusResponse.class);
            if (response != null) {
                PaymentStatusType paymentStatus = response.getStatus();
                delegateTask.setVariable("ePaymentStatus", paymentStatus.toString());
                if (PaymentStatusType.pending.equals(paymentStatus) ||
                        PaymentStatusType.inProcess.equals(paymentStatus)) {
                    throw new EFormsProcessEngineException(SeverityEnum.WARN, "PAYMENT_IS_STILL_PENDING");
                }
            }
        } catch (RestClientResponseException e) {
            delegateTask.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
            throw e;
        } catch (RestClientException e) {
            delegateTask.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "INTEGRATIONS.UNAVAILABLE", e.getMessage());
        }
    }
}
