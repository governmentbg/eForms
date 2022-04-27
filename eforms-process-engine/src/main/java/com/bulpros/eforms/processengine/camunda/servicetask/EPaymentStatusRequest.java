package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.epayment.model.PaymentRequestStatusRequest;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestStatusResponse;
import com.bulpros.eforms.processengine.epayment.model.PaymentRequestsById;
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
import java.util.Arrays;

@Setter
@Getter
@Named("ePaymentStatusRequest")
@Slf4j
public class EPaymentStatusRequest implements JavaDelegate {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    @Autowired
    private RestTemplate restTemplate;

    private Expression eServiceClientId;
    private Expression ePaymentId;
    private Expression ePaymentStatus;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String eServiceClientId = (String) this.getEServiceClientId().getValue(delegateExecution);
        String ePaymentId = (String) this.getEPaymentId().getValue(delegateExecution);

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
                delegateExecution.setVariable("ePaymentStatus", response.getPaymentStatuses().get(0).getStatus());
            }
        } catch (RestClientException e) {
            delegateExecution.setVariable("failure", "Failed request: " + e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

}
