
package com.bulpros.integrations.eDelivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageOnBehalfOfRequest {

    protected DcMessageDetails messageDetails;
    protected EProfileType senderType;
    protected String senderUniqueIdentifier;
    protected String senderPhone;
    protected String senderEmail;
    protected String senderFirstName;
    protected String senderLastName;
    protected EProfileType receiverType;
    protected String receiverUniqueIdentifier;
    protected String serviceOID;
    protected String operatorEGN;
}
