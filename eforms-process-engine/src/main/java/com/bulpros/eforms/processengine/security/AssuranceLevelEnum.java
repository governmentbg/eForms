package com.bulpros.eforms.processengine.security;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AssuranceLevelEnum {
    LOW ("Ниско", 1),
    SUBSTANTIAL ("Значително", 2),
    HIGH ("Високо", 3),
    NONE ("-- Не изисква --", 0);

    private String type;
    private int level;

    AssuranceLevelEnum(String type, int level){
        this.type = type;
        this.level = level;
    }

    public static AssuranceLevelEnum getEnumByType(String type) {
        return Arrays
                .stream(AssuranceLevelEnum.values())
                .filter(e -> e.type.equalsIgnoreCase(type))
                .findAny()
                .orElse(null);
    }
}
