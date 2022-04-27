package com.bulpros.eforms.processengine.edelivery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageRequest {

    private DcMessageDetails messageDetails;
    private EProfileType receiverType;
    private String receiverUniqueIdentifier;
    private String receiverPhone;
    private String receiverEmail;
    private String serviceOID;
    private String operatorEGN;
}
