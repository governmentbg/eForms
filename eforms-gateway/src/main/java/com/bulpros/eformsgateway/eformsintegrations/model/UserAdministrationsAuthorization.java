package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAdministrationsAuthorization implements Serializable {
    private static final long serialVersionUID = 1L;

    private String result;
    private UserAdministrationsAuthorizationProfile profile;
}
