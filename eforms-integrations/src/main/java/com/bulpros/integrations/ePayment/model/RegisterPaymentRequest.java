package com.bulpros.integrations.ePayment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterPaymentRequest {

    private List<PayerProfile> actors;
    private PaymentData paymentData;

}

