package com.bulpros.eforms.processengine.egov.model.eservice;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum YesNoEnum {
    YES ("Да", true),
    NO ("Не", false);

    private String type;
    private boolean value;

    YesNoEnum(String type, boolean value){
        this.type = type;
        this.value = value;
    }

    public static YesNoEnum getEnumByType(String type) {
        return Arrays
                .stream(YesNoEnum.values())
                .filter(e -> e.type.equalsIgnoreCase(type))
                .findAny()
                .orElse(null);
    }
}
