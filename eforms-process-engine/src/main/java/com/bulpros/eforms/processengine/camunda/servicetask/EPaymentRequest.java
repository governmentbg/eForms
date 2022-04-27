package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.eforms.processengine.camunda.repository.AuditlogRepository;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.epayment.model.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@Named("ePaymentRequest")
@Slf4j
public class EPaymentRequest implements JavaDelegate {

    private final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    @Autowired
    private CamundaProcessRepository processService;
    @Autowired
    protected ConfigurationProperties processConfProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuditlogRepository auditlogRepository;

    private Expression eServiceClientId;
    private Expression aisPaymentId;
    private Expression currency;
    private Expression paymentTypeCode;
    private Expression paymentAmount;
    private Expression paymentReason;
    private Expression applicantUinTypeId;
    private Expression applicantUin;
    private Expression applicantName;
    private Expression paymentReferenceType;
    private Expression paymentReferenceNumber;
    private Expression paymentReferenceDate;
    private Expression expirationDate;
    private Expression additionalInformation;
    private Expression administrativeServiceUri;
    private Expression administrativeServiceSupplierUri;

    private Expression ePaymentId;
    private Expression ePaymentRegistrationTime;
    private Expression ePaymentAccessCode;
    private Expression failure;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String eServiceClientId = (String) this.getEServiceClientId().getValue(delegateExecution);
        String aisPaymentId = (String) this.getAisPaymentId().getValue(delegateExecution);
        String currency = (String) this.getCurrency().getValue(delegateExecution);
        String paymentTypeCode = (String) this.getPaymentTypeCode().getValue(delegateExecution);
        String paymentAmount = (String) this.getPaymentAmount().getValue(delegateExecution);
        String paymentReason = (String) this.getPaymentReason().getValue(delegateExecution);
        String applicantUinTypeId = (String) this.getApplicantUinTypeId().getValue(delegateExecution);
        String applicantUin = (String) this.getApplicantUin().getValue(delegateExecution);
        String applicantName = (String) this.getApplicantName().getValue(delegateExecution);
        String paymentReferenceType = (String) this.getPaymentReferenceType().getValue(delegateExecution);
        String paymentReferenceNumber = (String) this.getPaymentReferenceNumber().getValue(delegateExecution);
        String paymentReferenceDate = (String) (this.getPaymentReferenceDate().getValue(delegateExecution));
        String expirationDate = (String) (this.getExpirationDate().getValue(delegateExecution));
        String additionalInformation = (String) this.getAdditionalInformation().getValue(delegateExecution);
        String administrativeServiceUri = (String) this.getAdministrativeServiceUri().getValue(delegateExecution);
        String administrativeServiceSupplierUri = (String) this.getAdministrativeServiceSupplierUri().getValue(delegateExecution);

        PaymentRequestRequest paymentRequestRequest = new PaymentRequestRequest();
        paymentRequestRequest.setEServiceClientId(eServiceClientId);
        String ePaymentCallbackURL = prepareCallbackURL(delegateExecution.getProcessInstanceId());
        PaymentRequest paymentRequest = new PaymentRequest(aisPaymentId, currency, paymentTypeCode,
                String.valueOf(paymentAmount), paymentReason, applicantUinTypeId, applicantUin,
                applicantName, paymentReferenceType, paymentReferenceNumber, paymentReferenceDate, expirationDate,
                additionalInformation, administrativeServiceUri, administrativeServiceSupplierUri,
                ePaymentCallbackURL);
        paymentRequestRequest.setPaymentRequest(paymentRequest);
        delegateExecution.setVariable("ePaymentCallbackURL", ePaymentCallbackURL);

        try {
            PaymentRequestResponse response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/payment-request").toUriString(),
                    paymentRequestRequest, PaymentRequestResponse.class);

            if (response.getAcceptedReceiptJson() != null) {
                delegateExecution.setVariable("ePaymentId", response.getAcceptedReceiptJson().getId());
                Date registrationTime = response.getAcceptedReceiptJson().getRegistrationTime();
                delegateExecution.setVariable("ePaymentRegistrationTime", registrationTime);
                delegateExecution.setVariable("ePaymentRegistrationTimeStr", dateTimeFormat.format(registrationTime));

                PaymentAccessCodeRequest paymentAccessCodeRequest = new PaymentAccessCodeRequest();
                paymentAccessCodeRequest.setEServiceClientId(eServiceClientId);
                PaymentRequestId paymentRequestId = new PaymentRequestId(response.getAcceptedReceiptJson().getId());
                paymentAccessCodeRequest.setPaymentRequestId(paymentRequestId);

                PaymentAccessCodeResponse accessCodeResponse = restTemplate.postForObject(
                        UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                                .path("/payment-request-access-code").toUriString(),
                        paymentAccessCodeRequest, PaymentAccessCodeResponse.class);

                delegateExecution.setVariable("ePaymentAccessCode", accessCodeResponse.getAccessCode());

                createAuditlogEvent(response.getAcceptedReceiptJson().getId(), delegateExecution);

            } else if (response.getUnacceptedReceiptJson() != null) {
                delegateExecution.setVariable("ePaymentId", null);
                delegateExecution.setVariable("ePaymentRegistrationTime", null);
                delegateExecution.setVariable("ePaymentRegistrationTimeStr", null);
                delegateExecution.setVariable("ePaymentAccessCode", null);
                // TODO
                if (response.getUnacceptedReceiptJson().getErrors().size() > 0) {
                    delegateExecution.setVariable("failure", response.getUnacceptedReceiptJson().getErrors().get(0));
                } else {
                    delegateExecution.setVariable("failure", "Failed request");
                }
            }
        } catch (RestClientException e) {
            delegateExecution.setVariable("ePaymentId", null);
            delegateExecution.setVariable("ePaymentRegistrationTime", null);
            delegateExecution.setVariable("ePaymentRegistrationTimeStr", null);
            delegateExecution.setVariable("ePaymentAccessCode", null);
            delegateExecution.setVariable("failure", "Failed request");
            log.error(e.getMessage(), e);
        }
    }

    private String prepareCallbackURL(String processInstanceId) {
        return processConfProperties.getEPaymentCallbackURL() +
                "?processId=" + processInstanceId +
                "&message=PaymentStatusMessage" +
                "&fieldId=ePaymentId";
    }

    private void createAuditlogEvent(String ePaymentId, DelegateExecution delegateExecution) {
        try {
            EventTypeEnum eventType = EventTypeEnum.AIS_EPAYMENT_REQUEST_SENT;
            String eventDescription = "Payment ID: " + ePaymentId;
            auditlogRepository.registerEvent(eventType, eventDescription, delegateExecution);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
