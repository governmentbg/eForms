package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectRegistrationResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean hasRegistration;
    private String identificator;
    private SubjectInfo subjectInfo;
}
