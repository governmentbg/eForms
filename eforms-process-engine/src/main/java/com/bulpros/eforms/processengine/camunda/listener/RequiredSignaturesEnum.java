package com.bulpros.eforms.processengine.camunda.listener;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum RequiredSignaturesEnum {
    REQUESTOR("requestorSignature"),
    REQUESTOR_AND_SIGNEES("requestorAndSigneesSignature"),
    SIGNEES("signeesSignature");
    
    private final String value;

    private RequiredSignaturesEnum(String value) {
        this.value = value;
    }
    
    public static RequiredSignaturesEnum getByValue(String value) {
        return Arrays
                .stream(RequiredSignaturesEnum.values())
                .filter(e -> e.value.equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }
}
