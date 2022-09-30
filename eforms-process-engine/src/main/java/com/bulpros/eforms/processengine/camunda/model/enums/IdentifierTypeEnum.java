package com.bulpros.eforms.processengine.camunda.model.enums;

import lombok.Getter;

@Getter
public enum IdentifierTypeEnum {
    
    PERSONAL_NUMBER("PNOBG"),
    PASSPORT_NUMBER("PASSBG"),
    ID_CARD_NUMBER("IDCBG"),
    ORGANIZATION_IDENTIFIER("VARBG"),
    BULSTAT_IDENTIFIER("NTRBG");
    

    private final String value;

    IdentifierTypeEnum(String value) {
        this.value = value;
    }
    
    public String getIdentifierFromNumber(String number) {
        return String.format("%s-%s", value, number);
    }
}
