package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalProfileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String personIdentifier;
    private String profileType;
    private String title = "";
    private String identifierType = "";
    private String identifier = "";
    private String status = "";
    private List<String> roles;
}
