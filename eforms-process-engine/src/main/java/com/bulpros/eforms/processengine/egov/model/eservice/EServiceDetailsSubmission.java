package com.bulpros.eforms.processengine.egov.model.eservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class EServiceDetailsSubmission {
    private String arId="";
    private String serviceName="";
    private String requiredSecurityLevel="";
    private String serviceDescription="";
    private List<String> meansOfIdentification;
    private String provisionEAddress="";
    private String processDefinitionId="";
    private String serviceOID="";
    private String url="";
    private String status="";

}
