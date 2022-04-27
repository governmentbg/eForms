package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAdministrationsAuthorizationProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String personalIdentifier;
    private List<Administration> administrations;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Administration implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String eik;
        private String profileType;
        private List<String> roles;
    }
}
