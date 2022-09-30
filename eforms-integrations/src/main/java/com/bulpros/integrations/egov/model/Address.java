package com.bulpros.integrations.egov.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {
    private Ekatte ekatte;
    private String description;
    private String full;
}
