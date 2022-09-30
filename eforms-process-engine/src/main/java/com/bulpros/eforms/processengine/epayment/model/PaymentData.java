package com.bulpros.eforms.processengine.epayment.model;


import com.bulpros.eforms.processengine.epayment.model.enums.PaymentStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
    private String reason;
    private String createDate;
    private String additionalInformation;
    private String administrativeServiceUri;
    private String administrativeServiceSupplierUri;
    private String administrativeServiceNotificationURL;
}

