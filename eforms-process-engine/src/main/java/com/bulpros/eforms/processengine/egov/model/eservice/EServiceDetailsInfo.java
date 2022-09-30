package com.bulpros.eforms.processengine.egov.model.eservice;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.bulpros.eforms.processengine.egov.model.supplier.SupplierList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EServiceDetailsInfo {
    private String id;
    private String serviceNumber;
    private String oid;
    private String securityLevel;
    private String isInternalAdminService;
    private String serviceName;
    private String modifiedDate;
    private String serviceDescription;
    private String shortName;
}
