package com.bulpros.eformsgateway.form.service;

import java.util.Arrays;

public enum LanguagesStatusEnum {
    PUBLIC("public"),
    HIDDEN("hidden"),
    ALL("all"),
    UNTRANSLATED("untranslated");

    public final String status;

    LanguagesStatusEnum(String status) {
        this.status = status;
    }

    public static boolean isLanguagesStatusValid(String status) {
        return Arrays.stream(LanguagesStatusEnum.values())
                .anyMatch(e -> e.status.equals(status));
    }
}
