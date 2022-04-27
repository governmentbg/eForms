package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.edelivery.model.SendMessageInReplyToRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageInReplyToResponse;
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
@Named("eDeliverySendMessageInReplyTo")
@Slf4j
@Scope("prototype")
public class EDeliverySendMessageInReplyTo extends EDeliverySend {

    private Expression replyToMessageId;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        prepareMessage(delegateExecution);

        Integer replyToMessageIdValue = (Integer) this.getReplyToMessageId().getValue(delegateExecution);

        SendMessageInReplyToRequest sendMessageInReplyToRequest = new SendMessageInReplyToRequest();
        sendMessageInReplyToRequest.setMessage(getMessage());
        sendMessageInReplyToRequest.setReplyToMessageId(replyToMessageIdValue);
        sendMessageInReplyToRequest.setServiceOID(getServiceOIDValue());

        SendMessageInReplyToResponse response = getRestTemplate().postForObject(
                UriComponentsBuilder.fromHttpUrl(getEDeliveryUrl() + getEDeliveryPath())
                        .path("/send-message-in-reply-to").toUriString(),
                sendMessageInReplyToRequest, SendMessageInReplyToResponse.class);

        if (response != null && response.getSendMessageInReplyToResult() != null) {
            delegateExecution.setVariable("eDeliverySendMessageInReplyToResult", response.getSendMessageInReplyToResult());
        } else {
            delegateExecution.setVariable("eDeliverySendMessageInReplyToResult", null);
        }

    }

}
