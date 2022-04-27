package com.bulpros.eformsgateway.form.service;

import java.util.Arrays;

public enum CaseStatusClassifierEnum {
    SERVICE_IN_APPLICATION("serviceInApplication"),
    SERVICE_IN_REQUEST("serviceInRequest"),
    SERVICE_IN_COMPLETION("serviceInCompletion");

    public final String classifier;

    private CaseStatusClassifierEnum(String classifier) {
        this.classifier = classifier;
    }

    public static CaseStatusClassifierEnum getCaseStatusEnumByStatus(String status) {
        return Arrays
                .stream(CaseStatusClassifierEnum.values())
                .filter(statusEnum -> statusEnum.classifier.equalsIgnoreCase(status))
                .findAny()
                .orElse(null);

    }
}
