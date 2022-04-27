
package com.bulpros.eforms.processengine.edelivery.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SendElectronicDocumentOnBehalfOfRequest {

    private String subject;
    private byte[] docBytes;
    private String docNameWithExtension;
    private String docRegNumber;
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
