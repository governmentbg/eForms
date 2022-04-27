package com.bulpros.eforms.processengine.camunda.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalProfileDto {
    private String personIdentifier;
    private String profileType;
    private String title = "";
    private String identifierType = "";
    private String identifier = "";
    private String status = "";
    private List<String> roles;
}
