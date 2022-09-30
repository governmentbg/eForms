
package com.bulpros.integrations.eDelivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageOnBehalfToPersonRequest {

    protected DcMessageDetails message;
    protected String senderUniqueIdentifier;
    protected String receiverUniqueIdentifier;
    protected String receiverPhone;
    protected String receiverEmail;
    protected String receiverFirstName;
    protected String receiverLastName;
    protected String serviceOID;
    protected String operatorEGN;
}
