package com.bulpros.eforms.processengine.egov.model.eservice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spinjar.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties
public class EService {
    private String supplier;
    private String arId;
    private String serviceOID;
    private String supplierEIK;
}
