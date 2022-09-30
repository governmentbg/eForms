package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.epayment.model.PaymentStatusResponse;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Named("ePaymentStatusRequest")
@Scope("prototype")
public class EPaymentStatusRequestDelegate implements JavaDelegate {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    private final RestTemplate restTemplate;

    private Expression eServiceClientId;
    private Expression ePaymentId;
    private Expression ePaymentStatus;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String ePaymentId = (String) this.getEPaymentId().getValue(delegateExecution);
        try {
            PaymentStatusResponse response = restTemplate.getForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-status").queryParam("paymentId", ePaymentId).toUriString(),
                    PaymentStatusResponse.class);
            if (response != null) {
                delegateExecution.setVariable("ePaymentStatus", response.getStatus().toString());
            }
        } catch (RestClientResponseException e) {
            delegateExecution.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
            throw e;
        } catch (RestClientException e) {
            delegateExecution.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "INTEGRATIONS.UNAVAILABLE", e.getMessage());
        }
    }
}
