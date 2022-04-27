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
    private SupplierList suppliers;
    private IdentificationMethods identificationMethods;
    private String language;
    private String onlineApplicationDescription;
    private String oid;
    private String securityLevel;
//    private PossibleWaysToApply possibleWaysToApply;
    private String result;
    private String administrationAuthority;
    private String isInternalAdminService;
    private String eauApplicationFormLink;
    private String prepareInAdvance;
    private String terms;
    private String applicationForm;
//    private AdministrativeSupplyUnits administrativeSupplyUnits;
    private String serviceName;
//    private Classification classification;
    private String sectionNameAR;
    private String regulatoryAct;
    private String administrativeInfoUnits;
    private String modifiedDate;
    private String serviceDescription;
    private String shortName;

//    @JsonProperty("administrativeSupplyUnits")
//    public void setAdministrativeSupplyUnits(Object administrativeSupplyUnits) {
//        if(administrativeSupplyUnits instanceof String){
//            this.administrativeSupplyUnits = null;
//        }
//        else {
//            ObjectMapper objectMapper = new ObjectMapper();
//            try{
//
//            byte[] bytes = objectMapper.writeValueAsBytes(administrativeSupplyUnits);
//            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//            AdministrativeSupplyUnits administrativeSupplyUnitsParsed = objectMapper.readValue(bytes, AdministrativeSupplyUnits.class);
//            this.administrativeSupplyUnits = administrativeSupplyUnitsParsed;
//            }catch (Exception e){
//                log.error(e.getMessage());
//            }
//        }
//    }

    @JsonProperty("identificationMethods")
    public void setIdentificationMethods(Object identificationMethods) {
        if(identificationMethods instanceof String){
            this.identificationMethods = null;
        }
        else {
            ObjectMapper objectMapper = new ObjectMapper();
            try{
                LinkedHashMap<Object, String> map = (LinkedHashMap<Object, String>)identificationMethods;
                if(map.get("identificationMethod") instanceof String){
                    this.identificationMethods = new IdentificationMethods();
                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.add((String)map.get(0));
                }
                else {
                    byte[] bytes = objectMapper.writeValueAsBytes(identificationMethods);
                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    IdentificationMethods identificationMethodsParsed = objectMapper.readValue(bytes, IdentificationMethods.class);
                    this.identificationMethods = identificationMethodsParsed;
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
