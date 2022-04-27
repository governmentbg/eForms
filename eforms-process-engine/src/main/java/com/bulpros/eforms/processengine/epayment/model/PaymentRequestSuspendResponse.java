package com.bulpros.eforms.processengine.epayment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestSuspendResponse {

    String message;

    public PaymentRequestSuspendResponse(String message) {
        this.message = message;
    }
}
