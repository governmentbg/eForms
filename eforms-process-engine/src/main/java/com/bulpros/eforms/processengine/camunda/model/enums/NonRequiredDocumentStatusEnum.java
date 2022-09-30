package com.bulpros.eforms.processengine.camunda.model.enums;

import java.util.Arrays;

public enum NonRequiredDocumentStatusEnum {
    NON_REQUIRED("non-required"),
    NON_REQUIRED_FROM_MAIN("non-required-from-main"),
    NON_REQUIRED_FROM_USER_CHOICE("non-required-from-user-choice");

    public String status;

    NonRequiredDocumentStatusEnum(String status) {
        this.status = status;
    }

    public static NonRequiredDocumentStatusEnum getEnumByStatus(String status) {
        return Arrays
                .stream(NonRequiredDocumentStatusEnum.values())
                .filter(e -> e.status.equals(status))
                .findAny()
                .orElse(null);
    }
}

