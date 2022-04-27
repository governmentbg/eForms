package com.bulpros.eformsgateway.user.model;

import java.io.Serializable;
import java.util.List;

import com.bulpros.eformsgateway.form.service.AssuranceLevelEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String personIdentifier;
    private String ucn;
    private String keycloakId;
    private String token;
    private AssuranceLevelEnum assuranceLevel;
    private List<String> groups;
}
