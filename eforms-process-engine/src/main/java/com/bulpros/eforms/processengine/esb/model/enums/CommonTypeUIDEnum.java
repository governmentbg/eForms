package com.bulpros.eforms.processengine.esb.model.enums;

public enum CommonTypeUIDEnum {
    EGN("1"),
    BULSTAT("3"),
    EIK(""),
    OID("");

    private final String ePaymentValue;

    CommonTypeUIDEnum(String ePaymentValue) {
        this.ePaymentValue = ePaymentValue;
    }

    public String getId() {
        return name();
    }

    public String getEPaymentValue() {
        return ePaymentValue;
    }

    public static CommonTypeUIDEnum fromEPaymentValue(String ePaymentValue) {
        for (CommonTypeUIDEnum uid : CommonTypeUIDEnum.values()) {
            if (uid.ePaymentValue.equalsIgnoreCase(ePaymentValue)) {
                return uid;
            }
        }
        return null;
    }
}
