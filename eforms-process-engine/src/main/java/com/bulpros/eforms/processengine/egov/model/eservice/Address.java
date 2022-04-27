package com.bulpros.eforms.processengine.egov.model.eservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String districtName;
    private String settlementName;
    private String areaName;
    private String addressText;
    private String postCode;
    private String municipalityName;
}
