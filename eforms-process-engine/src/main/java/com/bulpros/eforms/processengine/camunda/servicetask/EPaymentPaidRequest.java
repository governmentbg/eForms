package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.epayment.model.PaymentRequestPaid;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestPaidRequest;
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
@Named("EPaymentPaidRequest")
@Slf4j
public class EPaymentPaidRequest implements JavaDelegate {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    @Autowired
    private RestTemplate restTemplate;

    private Expression eServiceClientId;
    private Expression ePaymentId;
    private Expression ePaymentMethod;
    private Expression ePaymentDescription;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String eServiceClientId = (String) this.getEServiceClientId().getValue(delegateExecution);
        String ePaymentId = (String) this.getEPaymentId().getValue(delegateExecution);
        String ePaymentMethod = (String) this.getEPaymentMethod().getValue(delegateExecution);
        String ePaymentDescription = (String) this.getEPaymentDescription().getValue(delegateExecution);

        PaymentRequestPaidRequest paidRequest = new PaymentRequestPaidRequest();
        paidRequest.setEServiceClientId(eServiceClientId);
        PaymentRequestPaid paid = new PaymentRequestPaid(ePaymentId, ePaymentMethod, ePaymentDescription);
        paidRequest.setPaymentRequest(paid);
        try {
            PaymentRequestSuspendResponse response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-request-paid").toUriString(),
                    paidRequest, PaymentRequestSuspendResponse.class);

        } catch (RestClientException e) {
            delegateExecution.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

}
