package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.epayment.model.enums.PaymentStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusCallbackRequest {

    String id;
    PaymentStatusType status;
    Date changeTime;

    String processId;
    String message;
    String fieldId;
}
