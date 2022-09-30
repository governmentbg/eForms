package com.bulpros.eformsgateway.eformsintegrations.exception;

public class InvalidPaymentStatusMessageException extends RuntimeException {

    public static final String INVALID_PAYMENT_STATUS =
            "INVALID PAYMENT STATUS (One of Pending, Authorized, Ordered, Paid, expired, Canceled, Suspended expected)";
    public static final String INVALID_PAYMENT_CHANGE_TIME_FORMAT =
            "INVALID_PAYMENT_CHANGE_TIME_FORMAT (Expected formats: yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX or yyyy-MM-dd'T'HH:mm:ss.SSSSSSS)";

    private static final long serialVersionUID = 1L;

    public InvalidPaymentStatusMessageException(String message) {
        super(message);
    }

}
