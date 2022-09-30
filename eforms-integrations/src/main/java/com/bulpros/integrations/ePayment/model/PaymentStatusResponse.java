package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.ePayment.model.enums.PaymentStatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PaymentStatusResponse {

    private String paymentId;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private PaymentStatusType status;
    private Date changeTime;

}