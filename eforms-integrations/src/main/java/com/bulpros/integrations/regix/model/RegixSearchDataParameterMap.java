package com.bulpros.integrations.regix.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class RegixSearchDataParameterMap {

    Map<String, RegixSearchDataParameter> parameters = new LinkedHashMap<>();

    @JsonAnySetter
    public void setParameter(String key, RegixSearchDataParameter value) {
        parameters.put(key, value);
    }

}
