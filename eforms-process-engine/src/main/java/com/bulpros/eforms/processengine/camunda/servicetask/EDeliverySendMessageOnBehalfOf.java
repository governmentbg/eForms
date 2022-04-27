package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.edelivery.model.EProfileType;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfOfRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfOfResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.springframework.context.annotation.Scope;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Setter
@Getter
@Named("eDeliverySendMessageOnBehalfOf")
@Slf4j
@Scope("prototype")
public class EDeliverySendMessageOnBehalfOf extends EDeliverySend {

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


        SendMessageOnBehalfOfResponse response = getRestTemplate().postForObject(
                UriComponentsBuilder.fromHttpUrl(getEDeliveryUrl() + getEDeliveryPath())
                        .path("/send-message-on-behalf-of").toUriString(),
                sendMessageOnBehalfOfRequest, SendMessageOnBehalfOfResponse.class);

        if (response != null && response.getSendMessageOnBehalfOfResult() != null) {
            delegateExecution.setVariable("eDeliverySendMessageResult", response.getSendMessageOnBehalfOfResult());
        } else {
            delegateExecution.setVariable("eDeliverySendMessageResult", null);
        }

    }

}
