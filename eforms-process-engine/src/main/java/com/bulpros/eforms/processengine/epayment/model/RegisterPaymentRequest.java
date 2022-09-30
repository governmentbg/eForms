package com.bulpros.eforms.processengine.epayment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RegisterPaymentRequest {

    private List<PayerProfile> actors;
    private PaymentData paymentData;

}

