package com.bulpros.eforms.processengine.epayment.model;

import com.bulpros.eforms.processengine.esb.model.CommonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentModel {

    private String requestId;
    private PaymentData paymentData;
    private CommonTypeInfo providerInfo;
    private String eserviceAisName;

}

