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
public class LegalPersonRegistrationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean hasRegistration;
    private String eik;
    private String email;
    private String name;
    private String phone;
    private List<EDeliveryProfile> profiles;

    @JsonProperty("profilesWithAccess")
    private void unpackProfilesWithAccess(Map<String, Object> profilesWithAccess) {
        ObjectMapper objectMapper = new ObjectMapper();
        this.profiles = objectMapper.convertValue(profilesWithAccess.get("dcSubjectShortInfo"), new TypeReference<List<EDeliveryProfile>>() {
        });
    }

}
