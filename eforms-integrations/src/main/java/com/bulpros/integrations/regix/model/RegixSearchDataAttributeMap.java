package com.bulpros.integrations.regix.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class RegixSearchDataAttributeMap {

    Map<String, String> attributes = new LinkedHashMap<>();

    @JsonAnySetter
    public void setParameter(String key, String value) {
        attributes.put(key, value);
    }

}
