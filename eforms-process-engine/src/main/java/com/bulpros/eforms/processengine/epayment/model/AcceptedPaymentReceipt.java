package com.bulpros.eforms.processengine.epayment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AcceptedPaymentReceipt {

    private String id;
    private Date registrationTime;

}
