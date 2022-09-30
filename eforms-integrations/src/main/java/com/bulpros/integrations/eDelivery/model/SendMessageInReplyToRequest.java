
package com.bulpros.integrations.eDelivery.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendMessageInReplyToRequest {

    protected DcMessageDetails message;
    protected Integer replyToMessageId;
    protected String serviceOID;
    protected String operatorEGN;
}
