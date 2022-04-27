package com.bulpros.eforms.processengine.epayment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentStatus {

    String id;
    String status;
    Date changeTime;

}
