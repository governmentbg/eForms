package com.bulpros.formio.repository.formio;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SubmissionFilterClauseEnum {

    NONE(""),
    NE("__ne"),
    GT("__gt"),
    GTE("__gte"),
    LT("__lt"),
    LTE("__lte"),
    IN("__in"),
    NIN("__nin"),
    EXISTS("__exists"),
    REGEX("__regex");
    
    private String value;
    
    SubmissionFilterClauseEnum(String value) {
        this.value = value;
    }

    public static SubmissionFilterClauseEnum getByValue(String value) {
        return Arrays
                .stream(SubmissionFilterClauseEnum.values())
                .filter(e -> e.value.equals(value))
                .findAny()
                .orElse(null);
    }
}

