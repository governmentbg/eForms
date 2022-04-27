package com.bulpros.eformsgateway.form.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAdditionalProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    private String personIdentifier;
    private String profileType;
    private String title;
    private String identifierType;
    private String identifier;
    private String status;
    private List<String> roles;
}
