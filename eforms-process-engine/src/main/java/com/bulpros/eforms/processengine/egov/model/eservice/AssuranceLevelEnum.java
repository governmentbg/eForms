package com.bulpros.eforms.processengine.egov.model.eservice;

import java.util.Arrays;

public enum AssuranceLevelEnum {
    LOW ("Ниско"),
    SUBSTANTIAL ("Значително"),
    HIGH ("Високо"),
    NONE ("-- Не изисква --");

    private String type;

    AssuranceLevelEnum (String type){
        this.type = type;
    }

    public static AssuranceLevelEnum getEnumByType(String type) {
        return Arrays
                .stream(AssuranceLevelEnum.values())
                .filter(e -> e.type.equalsIgnoreCase(type))
                .findAny()
                .orElse(null);
    }
}
