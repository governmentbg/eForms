package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;

@Getter
public enum IdentifierTypeEnum {

    EGN("1", "PNOBG"),
    LNCH("2", "PNOBG"),
    PASSPORT("", "PASSBG"),
    ID_CARD("", "IDCBG"),
    ORGANIZATION("6", "VARBG"),
    BULSTAT("7", "NTRBG");


    private final String code;
    private final String prefix;

    IdentifierTypeEnum(String code, String prefix) {
        this.code = code;
        this.prefix = prefix;
    }

    public String getIdentifierFromNumber(String number) {
        return String.format("%s-%s", prefix, number);
    }
}
