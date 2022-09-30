package com.bulpros.eformsgateway.process.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCaseMessageRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String businessKey;
    private String providerOID;
    @NotNull
    private String messageName;
    private String tenantId;
    private Boolean withoutTenantId;
    @NotNull
    private HashMap<String, Object> processVariables;
}
