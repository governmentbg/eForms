package com.bulpros.eformsgateway.eformsintegrations.model;

import lombok.Getter;

@Getter
public enum EDeliveryStatusEnum {
    OK("OK"),
    PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),
    SERVICE_NOT_AVAILABLE("SERVICE_NOT_AVAILABLE"),
    NOT_AUTHORIZED("NOT_AUTHORIZED");
    
    private String value;
    
    EDeliveryStatusEnum(String value) {
        this.value = value;
    }
}
