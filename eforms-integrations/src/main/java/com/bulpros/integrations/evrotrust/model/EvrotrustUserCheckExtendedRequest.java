package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EvrotrustUserCheckExtendedRequest {

    private String vendorNumber;
    private UserCheckExtendedRequest user;

    public EvrotrustUserCheckExtendedRequest(String vendorNumber, UserCheckExtendedRequest request) {
        this.vendorNumber = vendorNumber;
        this.user = new UserCheckExtendedRequest(request.getIdentificationNumber(),
                request.getCountry(), request.getEmail(), request.getPhone());
    }
}
