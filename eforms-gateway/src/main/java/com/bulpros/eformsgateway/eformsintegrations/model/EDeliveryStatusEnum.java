package com.bulpros.eformsgateway.eformsintegrations.model;

import lombok.Getter;

@Getter
public enum EDeliveryStatusEnum {
    OK("OK"),
    PROFILE_NOT_FOUND("ERROR.GATEWAY.EDELIVERY.PROFILE_NOT_FOUND"),
    SERVICE_NOT_AVAILABLE("ERROR.GATEWAY.EDELIVERY.SERVICE_NOT_AVAILABLE"),
    NOT_AUTHORIZED("ERROR.GATEWAY.EDELIVERY.NOT_AUTHORIZED");
    
    private String value;
    
    EDeliveryStatusEnum(String value) {
        this.value = value;
    }
}
