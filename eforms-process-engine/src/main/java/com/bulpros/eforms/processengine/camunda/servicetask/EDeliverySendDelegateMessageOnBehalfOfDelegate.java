package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.EProfileType;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfOfRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfOfResponse;
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
@Named("eDeliverySendMessageOnBehalfOf")
@Scope("prototype")
public class EDeliverySendDelegateMessageOnBehalfOfDelegate extends EDeliverySendDelegate {

    @Autowired
    public EDeliverySendDelegateMessageOnBehalfOfDelegate(MinioService minioService,
                                                          CamundaProcessRepository processService,
                                                          ConfigurationProperties processConfProperties,
                                                          RestTemplate restTemplate) {
        super(minioService, processService, processConfProperties, restTemplate);
    }

    private Expression senderType;
    private Expression senderPhone;
    private Expression senderEmail;
    private Expression senderFirstName;
    private Expression senderLastName;
    private String senderPhoneValue;
    private String senderEmailValue;
    private String senderFirstNameValue;
    private String senderLastNameValue;
    private Expression receiverType;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        prepareMessage(delegateExecution);

        String senderTypeValue = (String) this.getSenderType().getValue(delegateExecution);

        if (this.getSenderPhone() != null) {
            senderPhoneValue = (String) this.getSenderPhone().getValue(delegateExecution);
        }
        if (this.getSenderEmail() != null) {
            senderEmailValue = (String) this.getSenderEmail().getValue(delegateExecution);
        }
        if (this.getSenderFirstName() != null) {
            senderFirstNameValue = (String) this.getSenderFirstName().getValue(delegateExecution);
        }
        if (this.getSenderLastName() != null) {
            senderLastNameValue = (String) this.getSenderLastName().getValue(delegateExecution);
        }

        String receiverTypeValue = (String) this.getReceiverType().getValue(delegateExecution);

        SendMessageOnBehalfOfRequest sendMessageOnBehalfOfRequest = new SendMessageOnBehalfOfRequest();
        sendMessageOnBehalfOfRequest.setMessageDetails(getMessage());
        sendMessageOnBehalfOfRequest.setSenderType(EProfileType.fromValue(senderTypeValue));
        sendMessageOnBehalfOfRequest.setSenderUniqueIdentifier(getSenderUniqueIdentifierValue());
        sendMessageOnBehalfOfRequest.setSenderPhone(getSenderPhoneValue());
        sendMessageOnBehalfOfRequest.setSenderEmail(getSenderEmailValue());
        sendMessageOnBehalfOfRequest.setSenderFirstName(getSenderFirstNameValue());
        sendMessageOnBehalfOfRequest.setSenderLastName(getSenderLastNameValue());
        sendMessageOnBehalfOfRequest.setReceiverType(EProfileType.fromValue(receiverTypeValue));
        sendMessageOnBehalfOfRequest.setReceiverUniqueIdentifier(getReceiverUniqueIdentifierValue());
        sendMessageOnBehalfOfRequest.setServiceOID(getServiceOIDValue());
        sendMessageOnBehalfOfRequest.setOperatorEGN(getOperatorEGNValue());

        try {
            SendMessageOnBehalfOfResponse response = getRestTemplate().postForObject(
                    UriComponentsBuilder.fromHttpUrl(getEDeliveryUrl() + getEDeliveryPath())
                            .path("/send-message-on-behalf-of").toUriString(),
                    sendMessageOnBehalfOfRequest, SendMessageOnBehalfOfResponse.class);

            if (response != null && response.getSendMessageOnBehalfOfResult() != null) {
                delegateExecution.setVariable("eDeliverySendMessageResult", response.getSendMessageOnBehalfOfResult());
            } else {
                delegateExecution.setVariable("eDeliverySendMessageResult", null);
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
