package com.bulpros.eforms.processengine.epayment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequestsByIdResponse {

    List<PaymentRequestItem> paymentRequests;

    @Getter
    @Setter
    static class PaymentRequestItem {
        String id;
        @JsonProperty("requestJson")
        PaymentRequest paymentRequest;
    }

}
