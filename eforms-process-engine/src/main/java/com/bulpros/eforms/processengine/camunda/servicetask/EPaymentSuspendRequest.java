package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.epayment.model.PaymentRequestId;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestSuspendRequest;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestSuspendResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Setter
@Getter
@Named("ePaymentSuspendRequest")
@Slf4j
public class EPaymentSuspendRequest implements JavaDelegate {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    @Autowired
    private RestTemplate restTemplate;

    private Expression eServiceClientId;
    private Expression ePaymentId;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String eServiceClientId = (String) this.getEServiceClientId().getValue(delegateExecution);
        String ePaymentId = (String) this.getEPaymentId().getValue(delegateExecution);

        PaymentRequestSuspendRequest suspendRequest = new PaymentRequestSuspendRequest();
        suspendRequest.setEServiceClientId(eServiceClientId);
        PaymentRequestId paymentRequestId = new PaymentRequestId();
        paymentRequestId.setPaymentRequestId(ePaymentId);
        suspendRequest.setPaymentRequestId(paymentRequestId);
        try {
            PaymentRequestSuspendResponse response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-request-suspend").toUriString(),
                    suspendRequest, PaymentRequestSuspendResponse.class);

        } catch (RestClientException e) {
            delegateExecution.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

}
