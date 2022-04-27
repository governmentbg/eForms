package com.bulpros.eforms.processengine.epayment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String aisPaymentId;
    private String currency;
    private String paymentTypeCode;
    private String paymentAmount;
    private String paymentReason;
    private String applicantUinTypeId;
    private String applicantUin;
    private String applicantName;
    private String paymentReferenceType;
    private String paymentReferenceNumber;
    private String paymentReferenceDate;
    private String expirationDate;
    private String additionalInformation;
    private String administrativeServiceUri;
    private String administrativeServiceSupplierUri;
    private String administrativeServiceNotificationURL;

}
