package com.bulpros.eforms.processengine.egov.model.eservice;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdministrativeSupplyUnit {
    private Address address;
    private String name;
    private String faxNumber;
    private String workingTime;
    private String interSettlementCallingCode;
    private String email;
    private String webSiteUrl;
    private Phone phones;
}
