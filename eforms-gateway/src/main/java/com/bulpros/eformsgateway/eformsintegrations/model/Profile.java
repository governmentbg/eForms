package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String identifier;
    private Address address;
    private String phone;
    private String email;
}
