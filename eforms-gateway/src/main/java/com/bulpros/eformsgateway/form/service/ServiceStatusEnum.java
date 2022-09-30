package com.bulpros.eformsgateway.form.service;

public enum ServiceStatusEnum {
    DEVELOPED   ("developed"),
    VALIDATED   ("validated"),
    APPROVED    ("approved"),
    PUBLISHED   ("published"),
    INACTIVE    ("inactive"),
    DRAFT       ("draft");

    public final String status;

    private ServiceStatusEnum(String status) {
        this.status = status;
    }
}
