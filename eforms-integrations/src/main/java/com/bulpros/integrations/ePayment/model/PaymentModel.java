package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.esb.model.CommonTypeInfo;
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

