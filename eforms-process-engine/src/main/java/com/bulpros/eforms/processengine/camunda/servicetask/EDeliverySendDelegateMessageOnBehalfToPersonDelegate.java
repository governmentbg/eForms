package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToPersonRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToPersonResponse;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Slf4j
@Setter
@Getter
@Named("eDeliverySendMessageOnBehalfToPerson")
@Scope("prototype")
public class EDeliverySendDelegateMessageOnBehalfToPersonDelegate extends EDeliverySendDelegate {

    @Autowired
    public EDeliverySendDelegateMessageOnBehalfToPersonDelegate(MinioService minioService,
                                                                CamundaProcessRepository processService,
                                                                ConfigurationProperties processConfProperties,
                                                                RestTemplate restTemplate) {
        super(minioService, processService, processConfProperties, restTemplate);
    }

    private Expression receiverPhone;
    private Expression receiverEmail;
    private Expression receiverFirstName;
    private Expression receiverLastName;
    private String receiverPhoneValue;
    private String receiverEmailValue;
    private String receiverFirstNameValue;
    private String receiverLastNameValue;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        prepareMessage(delegateExecution);


        if (this.getReceiverPhone() != null) {
            receiverPhoneValue = (String) this.getReceiverPhone().getValue(delegateExecution);
        }
        if (this.getReceiverEmail() != null) {
            receiverEmailValue = (String) this.getReceiverEmail().getValue(delegateExecution);
        }
        if (this.getReceiverFirstName() != null) {
            receiverFirstNameValue = (String) this.getReceiverFirstName().getValue(delegateExecution);
        }
        if (this.getReceiverLastName() != null) {
            receiverLastNameValue = (String) this.getReceiverLastName().getValue(delegateExecution);
        }

        SendMessageOnBehalfToPersonRequest sendMessageOnBehalfToPersonRequest = new SendMessageOnBehalfToPersonRequest();
        sendMessageOnBehalfToPersonRequest.setMessage(getMessage());
        sendMessageOnBehalfToPersonRequest.setSenderUniqueIdentifier(getSenderUniqueIdentifierValue());
        sendMessageOnBehalfToPersonRequest.setReceiverUniqueIdentifier(getReceiverUniqueIdentifierValue());
        sendMessageOnBehalfToPersonRequest.setReceiverEmail(getReceiverEmailValue());
        sendMessageOnBehalfToPersonRequest.setReceiverPhone(getReceiverPhoneValue());
        sendMessageOnBehalfToPersonRequest.setReceiverFirstName(getReceiverFirstNameValue());
        sendMessageOnBehalfToPersonRequest.setReceiverLastName(getReceiverLastNameValue());
        sendMessageOnBehalfToPersonRequest.setServiceOID(getServiceOIDValue());
        sendMessageOnBehalfToPersonRequest.setOperatorEGN(getOperatorEGNValue());

        try {
            SendMessageOnBehalfToPersonResponse response = getRestTemplate().postForObject(
                    UriComponentsBuilder.fromHttpUrl(getEDeliveryUrl() + getEDeliveryPath())
                            .path("/send-message-on-behalf-to-person").toUriString(),
                    sendMessageOnBehalfToPersonRequest, SendMessageOnBehalfToPersonResponse.class);

            if (response != null && response.getSendMessageOnBehalfToPersonResult() != null) {
                delegateExecution.setVariable("eDeliverySendMessageOnBehalfToPersonResult", response.getSendMessageOnBehalfToPersonResult());
            } else {
                delegateExecution.setVariable("eDeliverySendMessageOnBehalfToPersonResult", null);
            }
        } catch (RestClientResponseException exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        } catch (RestClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "INTEGRATIONS.UNAVAILABLE", exception.getMessage());
        }
    }
}
