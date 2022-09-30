package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.auditlog.model.EventTypeEnum;
import com.bulpros.eforms.processengine.camunda.repository.AuditlogRepository;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.epayment.model.*;
import com.bulpros.eforms.processengine.esb.model.CommonTypeUID;
import com.bulpros.eforms.processengine.esb.model.enums.CommonTypeActorEnum;
import com.bulpros.eforms.processengine.esb.model.enums.CommonTypeUIDEnum;
import com.bulpros.eforms.processengine.esb.model.enums.ParticipantTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Named("ePaymentRequest")
@Scope("prototype")
public class EPaymentRequestDelegate implements JavaDelegate {

    private final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    @Value("${com.bulpros.eforms-integrations.url}")
    private String ePaymentUrl;
    @Value("${com.bulpros.eforms-integrations.epayment.prefix}")
    private String ePaymentPath;

    private final CamundaProcessRepository processService;
    protected final ConfigurationProperties processConfProperties;
    private final RestTemplate restTemplate;
    private final AuditlogRepository auditlogRepository;

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

        String ePaymentId = (String) this.getEPaymentId().getValue(delegateExecution);
        String ePaymentRegistrationTime = (String) this.getEPaymentRegistrationTime().getValue(delegateExecution);
        String ePaymentAccessCode = (String) this.getEPaymentAccessCode().getValue(delegateExecution);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEServiceClientId(eServiceClientId);
        String ePaymentCallbackURL = prepareCallbackURL(delegateExecution.getProcessInstanceId());

        PayerProfile payerProfile = PayerProfile.builder()
                .participantType(ParticipantTypeEnum.APPLICANT)
                .type(CommonTypeActorEnum.PERSON)
                .uid(CommonTypeUID.builder().type(CommonTypeUIDEnum.fromEPaymentValue(applicantUinTypeId))
                        .value(applicantUin)
                        .build())
                .name(applicantName)
                .build();

        RegisterPaymentRequest registerPaymentRequest = RegisterPaymentRequest.builder()
                .actors(Collections.singletonList(payerProfile))
                .paymentData(PaymentData.builder()
                        .paymentId(aisPaymentId)
                        .currency(currency)
                        .typeCode(paymentTypeCode)
                        .amount(String.valueOf(paymentAmount))
                        .reason(paymentReason)
                        .referenceType(paymentReferenceType)
                        .referenceNumber(paymentReferenceNumber)
                        .referenceDate(paymentReferenceDate)
                        .expirationDate(expirationDate)
                        .additionalInformation(additionalInformation)
                        .administrativeServiceUri(administrativeServiceUri)
                        .administrativeServiceSupplierUri(administrativeServiceSupplierUri)
                        .administrativeServiceNotificationURL(ePaymentCallbackURL)
                        .build())
                .build();

        paymentRequest.setPaymentRequest(registerPaymentRequest);
        delegateExecution.setVariable("ePaymentCallbackURL", ePaymentCallbackURL);

        try {
            RegisterPaymentResponse response = restTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.ePaymentUrl + this.ePaymentPath)
                            .path("/register-payment-extended").toUriString(),
                    paymentRequest, RegisterPaymentResponse.class);

            if (response != null) {
                delegateExecution.setVariable(ePaymentId, response.getPaymentId());
                Date registrationTime = response.getRegistrationTime();
                delegateExecution.setVariable(ePaymentRegistrationTime, registrationTime);
                delegateExecution.setVariable(ePaymentRegistrationTime + "Str", dateTimeFormat.format(registrationTime));
                delegateExecution.setVariable(ePaymentAccessCode, response.getAccessCode());

                createAuditlogEvent(response.getPaymentId(), delegateExecution);

            }

        } catch (RestClientResponseException e) {
            log.error(e.getMessage(), e);
            String error = e.getResponseBodyAsString().replaceAll("[\\n\\t\\s]+", " ");
            error = URLEncoder.encode(error, StandardCharsets.UTF_8.toString());
            throw new BpmnError("ePaymentFailure", error);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new BpmnError("ePaymentFailure", "INTEGRATIONS.UNAVAILABLE");
        }
    }

    private String prepareCallbackURL(String processInstanceId) {
        return processConfProperties.getEPaymentCallbackURL() +
                "?processId=" + processInstanceId +
                "&message=PaymentStatusMessage" +
                "&fieldId=ePaymentId";
    }

    private void createAuditlogEvent(String ePaymentId, DelegateExecution delegateExecution) {
        EventTypeEnum eventType = EventTypeEnum.AIS_EPAYMENT_REQUEST_SENT;
        String eventDescription = "Payment ID: " + ePaymentId;
        auditlogRepository.registerEvent(eventType, eventDescription, delegateExecution);

    }

}
