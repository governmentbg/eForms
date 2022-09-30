package com.bulpros.integrations.egov.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profile {
    private String identifier;
    private Address address;
    private String phone;
    private String email;
}
