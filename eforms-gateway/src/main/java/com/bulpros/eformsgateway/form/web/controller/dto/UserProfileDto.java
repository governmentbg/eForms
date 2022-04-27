package com.bulpros.eformsgateway.form.web.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String personIdentifierType = "";
    private String personIdentifier = "";
    private String personName = "";
    private String ekatteNumber = "";
    private String districtCorrespondence = "";
    private String cityCorrespondence = "";
    private String municipalityCorrespondence = "";
    private String adrresslineCorrespondence = "";
    private String phone = "";
    private Boolean isActive = false;
    private String email = "";
    private boolean phoneAuthorised = false;
    private boolean emailAuthorised = false;
    private boolean ekatteAuthorised = false;
    private boolean adrresslineCorrespondenceAuthorised = false;
    private List<AdditionalProfileDto> profiles;

}