package com.bulpros.eformsgateway.eformsintegrations.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum EDeliveryProfileTypeEnum {
    PERSON("Person", "1") {
        @Override
        public EDeliveryProfile hasAccess(List<EDeliveryProfile> profiles, String identification) {
            return profiles.stream().filter(p -> (p.getEgn().equals(identification) &&
                    (p.getProfileType().equals(this))))
                    .findAny()
                    .orElse(null);
        }
    },
    LEGAL_PERSON("LegalPerson", "2"),
    INSTITUTION("Institution", "3"),
    ADMINISTRATOR("Administrator", "");

    @JsonValue
    private final String value;
    private final String eGovValue;
    private static final Map<String, EDeliveryProfileTypeEnum> BY_EGOV_VALUE = new HashMap<>();

    static {
        for (EDeliveryProfileTypeEnum e : values()) {
            BY_EGOV_VALUE.put(e.getEGovValue(), e);
        }
    }

    EDeliveryProfileTypeEnum(String value, String eGovValue) {
        this.value = value;
        this.eGovValue = eGovValue;
    }

    public static EDeliveryProfileTypeEnum valueOfByEGovValue(String value) {
        return BY_EGOV_VALUE.get(value);
    }

    public EDeliveryProfile hasAccess(List<EDeliveryProfile> profiles, String identification) {
        return profiles.stream().filter(p -> (p.getEik().equals(identification) &&
                (p.getProfileType().equals(this))))
                .findAny()
                .orElse(null);
    }

}
