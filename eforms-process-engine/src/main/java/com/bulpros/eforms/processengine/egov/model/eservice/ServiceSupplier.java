package com.bulpros.eforms.processengine.egov.model.eservice;

import com.bulpros.formio.dto.ResourceDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ServiceSupplier {
    private ResourceDto supplier;
    private boolean hasAdministrativeUnits=false;
    private String serviceSupplierStatus="";
    private List<AdministrativeUnits> easAdministrativeUnitsList;
    private String aisClientEPayment="";
    private String serviceProviderBank="";
    private String serviceProviderBIC="";
    private String serviceProviderIBAN="";
    private String aisClientIntegrationKey="";
}
