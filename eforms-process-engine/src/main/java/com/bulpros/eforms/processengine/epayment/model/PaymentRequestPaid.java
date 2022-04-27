package com.bulpros.eforms.processengine.epayment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestPaid {

    @JsonProperty("id")
    String paymentRequestId;
    String paymentMethod;
    String paymentDescription;

}
