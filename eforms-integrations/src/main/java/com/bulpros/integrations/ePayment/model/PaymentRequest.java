package com.bulpros.integrations.ePayment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

    String eServiceClientId;
    RegisterPaymentRequest paymentRequest;

}
