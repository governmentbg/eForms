package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonRegistrationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean hasRegistration;
    private String name;
    private String personIdentificator;
    private List<EDeliveryProfile> profiles;

    @JsonProperty("accessibleProfiles")
    private void unpackAccessibleProfiles(Map<String, Object> accessibleProfiles) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.profiles = objectMapper.convertValue(accessibleProfiles.get("dcSubjectShortInfo"), new TypeReference<List<EDeliveryProfile>>() {
        });
    }

}
