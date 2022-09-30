package com.bulpros.formio.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class User {
    private String personIdentifier;
    private String ucn;
    private String keycloakId;
    private List<String> groups;
    private Map<String, Object> claims;

    public String getClaimAsString(String key) {
        return (String) this.getClaims().get(key);
    }

}
