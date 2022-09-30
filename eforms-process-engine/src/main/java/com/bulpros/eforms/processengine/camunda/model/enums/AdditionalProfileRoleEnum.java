package com.bulpros.eforms.processengine.camunda.model.enums;

public enum AdditionalProfileRoleEnum {
    User("user"),
    Admin("admin"),
    ServiceManager("serviceManager"),
    MetadataManager("metadataManager");

    public final String role;

    private AdditionalProfileRoleEnum(String role) {
        this.role = role;
    }
}
