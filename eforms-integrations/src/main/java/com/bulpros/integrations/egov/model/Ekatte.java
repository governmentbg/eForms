package com.bulpros.integrations.egov.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ekatte {
    @JsonProperty(value = "ЕКАТТЕ", required = true)
    private String ekatte;
    @JsonProperty(value = "Община", required = false)
    private String municipality;
    @JsonProperty(value = "Област", required = false)
    private String region;
    @JsonProperty(value = "Населено място",required = false)
    private  String populatedPlace;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Ekatte(String notAuthorized) {
        this.ekatte = notAuthorized;
        this.municipality = notAuthorized;
        this.region = notAuthorized;
        this.populatedPlace = notAuthorized;
    }

    @JsonCreator
    public Ekatte(@JsonProperty("ЕКАТТЕ") String ekatte, @JsonProperty("Община") String municipality,
    @JsonProperty("Област") String region, @JsonProperty("Населено място") String populatedPlace) {
        this.ekatte = ekatte == null ? "" : ekatte;
        this.municipality = municipality == null ? "" : municipality;
        this.region = region == null ? "" : region;
        this.populatedPlace = populatedPlace == null ? "" : populatedPlace;
    }
}
