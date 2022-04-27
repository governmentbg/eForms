package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    private Ekatte ekatte;
    private String description;
    private String full;
}
