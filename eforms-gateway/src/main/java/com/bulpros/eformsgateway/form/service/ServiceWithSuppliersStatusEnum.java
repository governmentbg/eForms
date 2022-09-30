package com.bulpros.eformsgateway.form.service;

public enum ServiceWithSuppliersStatusEnum {
    ACTIVE      ("active"),
    PUBLISHED   ("published"),
    INACTIVE    ("inactive"),
    DRAFT       ("draft");

    public final String status;

    private ServiceWithSuppliersStatusEnum(String status) {
        this.status = status;
    }
}
