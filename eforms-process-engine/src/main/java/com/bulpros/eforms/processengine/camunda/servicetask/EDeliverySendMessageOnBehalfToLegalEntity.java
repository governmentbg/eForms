package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToLegalEntityRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToLegalEntityResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Scope;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Setter
@Getter
@Named("eDeliverySendMessageOnBehalfToLegalEntity")
@Slf4j
@Scope("prototype")
public class EDeliverySendMessageOnBehalfToLegalEntity extends EDeliverySend {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        prepareMessage(delegateExecution);

        SendMessageOnBehalfToLegalEntityRequest sendMessageOnBehalfToLegalEntityRequest = new SendMessageOnBehalfToLegalEntityRequest();
        sendMessageOnBehalfToLegalEntityRequest.setMessage(getMessage());
        sendMessageOnBehalfToLegalEntityRequest.setSenderUniqueIdentifier(getSenderUniqueIdentifierValue());
        sendMessageOnBehalfToLegalEntityRequest.setReceiverUniqueIdentifier(getReceiverUniqueIdentifierValue());
        sendMessageOnBehalfToLegalEntityRequest.setOperatorEGN(getOperatorEGNValue());
        sendMessageOnBehalfToLegalEntityRequest.setServiceOID(getServiceOIDValue());

        SendMessageOnBehalfToLegalEntityResponse response = getRestTemplate().postForObject(
                UriComponentsBuilder.fromHttpUrl(this.getEDeliveryUrl() + this.getEDeliveryPath())
                        .path("/send-message-on-behalf-to-legal-entity").toUriString(),
                sendMessageOnBehalfToLegalEntityRequest, SendMessageOnBehalfToLegalEntityResponse.class);

        if (response != null && response.getSendMessageOnBehalfToLegalEntityResult() != null) {
            delegateExecution.setVariable("eDeliverySendMessageOnBehalfToLegalEntityResult", response.getSendMessageOnBehalfToLegalEntityResult());
        } else {
            delegateExecution.setVariable("eDeliverySendMessageOnBehalfToLegalEntityResult", null);
        }
    }
}
