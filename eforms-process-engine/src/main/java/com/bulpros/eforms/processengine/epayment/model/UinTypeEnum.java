package com.bulpros.eforms.processengine.epayment.model;

public enum UinTypeEnum {
    EGN("1"),
    LNCH("2"),
    BULSTAT("3");

    private String ePaymentValue;

    UinTypeEnum(String ePaymentValue) {
        this.ePaymentValue = ePaymentValue;
    }

    public String getId() {
        return name();
    }

    public String getEPaymentValue() {
        return ePaymentValue;
    }

}
