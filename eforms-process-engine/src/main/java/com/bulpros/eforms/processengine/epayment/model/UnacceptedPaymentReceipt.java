package com.bulpros.eforms.processengine.epayment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UnacceptedPaymentReceipt {

    private Date validationTime;
    private List<String> errors = null;

}
