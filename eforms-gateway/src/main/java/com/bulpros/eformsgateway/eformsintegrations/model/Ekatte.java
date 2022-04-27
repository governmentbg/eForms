package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ekatte implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ЕКАТТЕ")
    private String ekatte;
    @JsonProperty("Община")
    private String municipality;
    @JsonProperty("Област")
    private String region;
    @JsonProperty("Населено място")
    private  String populatedPlace;
}
