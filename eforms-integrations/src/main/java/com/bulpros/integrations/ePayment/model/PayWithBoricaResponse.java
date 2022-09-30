package com.bulpros.integrations.ePayment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PayWithBoricaResponse {

    String clientId;
    String data;
    String hmac;

}
