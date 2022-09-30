package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCheckExtendedResponse {

    private Boolean isRegistered;
    private Boolean isIdentified;
    private Boolean isRejected;
    private Boolean isSupervised;
    private Boolean isReadyToSign;
    private Boolean hasConfirmedPhone;
    private Boolean hasConfirmedEmail;

}
