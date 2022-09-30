package com.bulpros.integrations.regix.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegixSearchDataContext {

    @JsonProperty(value = "ServiceURI", required = true)
    private String serviceURI;
    @JsonProperty(value = "ServiceType", required = true)
    private String serviceType;
    @JsonProperty(value = "EmployeeIdentifier")
    private String employeeIdentifier;
    @JsonProperty(value = "EmployeeNames")
    private String employeeNames;
    @JsonProperty(value = "EmployeeAditionalIdentifier")
    private String employeeAditionalIdentifier;
    @JsonProperty(value = "EmployeePosition")
    private String employeePosition;
    @JsonProperty(value = "AdministrationOId")
    private String administrationOId;
    @JsonProperty(value = "AdministrationName")
    private String administrationName;
    @JsonProperty(value = "ResponsiblePersonIdentifier")
    private String responsiblePersonIdentifier;
    @JsonProperty(value = "LawReason", required = true)
    private String lawReason;
    @JsonProperty(value = "Remark")
    private String remark;

}
