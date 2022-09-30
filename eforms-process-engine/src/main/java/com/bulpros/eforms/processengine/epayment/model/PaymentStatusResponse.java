package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.epayment.model.enums.PaymentStatusType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentStatusResponse {

    private String paymentId;
    private PaymentStatusType status;
    private Date changeTime;

}