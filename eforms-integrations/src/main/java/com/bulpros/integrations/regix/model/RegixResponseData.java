package com.bulpros.integrations.regix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class RegixResponseData {

    @JsonProperty(value = "Response")
    private Map<String, Object> responseData = new LinkedHashMap<>();

}
