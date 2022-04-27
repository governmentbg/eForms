package com.bulpros.eforms.processengine.epayment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestSuspendRequest {

    String eServiceClientId;
    PaymentRequestId paymentRequestId;

}
