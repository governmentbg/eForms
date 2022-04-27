package com.bulpros.eforms.processengine.edelivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageOnBehalfOfRequest {

    private DcMessageDetails messageDetails;
    private EProfileType senderType;
    private String senderUniqueIdentifier;
    private String senderPhone;
    private String senderEmail;
    private String senderFirstName;
    private String senderLastName;
    private EProfileType receiverType;
    private String receiverUniqueIdentifier;
    private String serviceOID;
    private String operatorEGN;
}
