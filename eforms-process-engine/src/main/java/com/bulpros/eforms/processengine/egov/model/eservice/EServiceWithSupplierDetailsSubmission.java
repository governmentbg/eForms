package com.bulpros.eforms.processengine.egov.model.eservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EServiceWithSupplierDetailsSubmission {
    private String arId="";
    private String supplierEAS="";
    private String serviceTitle="";
    private String serviceSupplierTitle="";
    private String serviceDescription="";
    private String status="";
}
