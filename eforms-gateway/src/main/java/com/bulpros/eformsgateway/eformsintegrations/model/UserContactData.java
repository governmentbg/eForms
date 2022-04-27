package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserContactData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String result;
    private Profile profile;
    private String message;
}
