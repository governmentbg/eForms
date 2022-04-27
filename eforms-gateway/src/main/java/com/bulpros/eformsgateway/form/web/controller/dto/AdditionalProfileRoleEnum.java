package com.bulpros.eformsgateway.form.web.controller.dto;

public enum AdditionalProfileRoleEnum {
    User("user"),
    Admin("admin"),
    ServiceManager("servicemanager"),
    MetadataManager("metadatamanager"),
    CashAdmin("cashadmin");

    public final String role;

    private AdditionalProfileRoleEnum(String role) {
        this.role = role;
    }
}
