package com.bulpros.integrations.ePayment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RegisterPaymentResponse {

    private String paymentId;
    private Date registrationTime;
    private String accessCode;

}

