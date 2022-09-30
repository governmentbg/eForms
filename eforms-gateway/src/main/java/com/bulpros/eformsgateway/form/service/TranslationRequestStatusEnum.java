package com.bulpros.eformsgateway.form.service;

public enum TranslationRequestStatusEnum {
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    ERROR("ERROR");

    public final String status;

    TranslationRequestStatusEnum(String status) {
        this.status = status;
    }
}
