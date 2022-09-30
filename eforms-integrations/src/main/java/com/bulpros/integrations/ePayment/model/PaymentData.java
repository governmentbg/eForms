package com.bulpros.integrations.ePayment.model;

import com.bulpros.integrations.ePayment.model.enums.PaymentStatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentData {

    private String paymentId;
    private String currency;
    private String amount;
    private PaymentStatusType status;
    private String typeCode;
    private String referenceNumber;
    private String referenceType;
    private String referenceDate;
    private String expirationDate;
    private String createDate;
    private String reason;
    private String additionalInformation;
    private String administrativeServiceUri;
    private String administrativeServiceSupplierUri;
    private String administrativeServiceNotificationURL;


}

