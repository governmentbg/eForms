package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String electronicSubjectId;
    private String electronicSubjectName;
    private String email;
    private Boolean isActivated;
    private String phoneNumber;
    private String profileType;
    private String institutionType;
}
