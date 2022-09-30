package com.bulpros.eformsgateway.form.service;

public enum ServiceSupplierStatusEnum {
    PUBLISHED("published"),
    ACTIVE("active"),
    INACTIVE("inactive"),
    DRAFT("draft");

    public final String status;

    private ServiceSupplierStatusEnum(String status) {
        this.status = status;
    }
}
