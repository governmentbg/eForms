package com.bulpros.eformsgateway.process.web.dto;

public enum PaymentStatusEnum {
    pending, authorized, ordered, paid, expired, canceled, suspended, inProcess;

    public static PaymentStatusEnum findByName(String name) {
        PaymentStatusEnum result = null;
        for (PaymentStatusEnum direction : values()) {
            if (direction.name().equalsIgnoreCase(name)) {
                result = direction;
                break;
            }
        }
        return result;
    }
}
