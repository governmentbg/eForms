package com.bulpros.integrations.auditlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditlogServiceRequest {

    private String serviceOID;
    //private String spoid;
    private String spName;
    private String serviceName;
    private String authnRequesterOID;
    private String adminiOID;
    private String adminLegalName;

}
