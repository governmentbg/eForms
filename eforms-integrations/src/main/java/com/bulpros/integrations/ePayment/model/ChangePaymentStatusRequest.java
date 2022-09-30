package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.ePayment.model.enums.PaymentMethodEnum;
import com.bulpros.integrations.ePayment.model.enums.PaymentStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePaymentStatusRequest {

    private String paymentId;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private PaymentStatusEnum status;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private PaymentMethodEnum method;
    private String description;
}

