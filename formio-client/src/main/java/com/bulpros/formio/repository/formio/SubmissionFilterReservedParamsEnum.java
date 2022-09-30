package com.bulpros.formio.repository.formio;

import java.util.Arrays;

public enum SubmissionFilterReservedParamsEnum {
    
    LIMIT("limit"),
    SKIP("skip"),
    SORT("sort"),
    SELECT("select");
    
    private String value;
    
    SubmissionFilterReservedParamsEnum(String value) {
        this.value = value;
    }

    public static SubmissionFilterReservedParamsEnum getByValue(String value) {
        return Arrays
                .stream(SubmissionFilterReservedParamsEnum.values())
                .filter(e -> e.value.equals(value))
                .findAny()
                .orElse(null);
    }
}
