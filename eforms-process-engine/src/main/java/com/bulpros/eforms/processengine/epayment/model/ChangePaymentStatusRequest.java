package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.epayment.model.enums.PaymentMethodEnum;
import com.bulpros.eforms.processengine.epayment.model.enums.PaymentStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePaymentStatusRequest {

    private String paymentId;
    private PaymentStatusEnum status;
    private PaymentMethodEnum method;
    private String description;
}

