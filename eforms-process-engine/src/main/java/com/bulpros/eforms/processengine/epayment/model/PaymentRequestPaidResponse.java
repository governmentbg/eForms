package com.bulpros.eforms.processengine.epayment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestPaidResponse {

    String message;

    public PaymentRequestPaidResponse(String message) {
        this.message = message;
    }
}
