package com.bulpros.eformsgateway.form.service;

public enum ServiceStatusEnum {
    ACTIVE("active"),
    INACTIVE("inactive"),
    DRAFT("draft");

    public final String status;

    private ServiceStatusEnum(String status) {
        this.status = status;
    }
}
