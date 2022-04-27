package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.egov.model.eservice.EServiceContainer;
import com.bulpros.eforms.processengine.egov.model.eservice.EServiceDetails;
import com.bulpros.eforms.processengine.egov.model.eservice.EServiceList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EServiceServiceImpl implements EServiceService {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String integrationsUrl;
    @Value("${com.bulpros.eforms-integrations.egov.prefix}")
    private String egovPrefix;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public EServiceList getServicesBySupplierEIK(String supplierEIK) {
        LinkedHashMap<Object,Object> eServiceContainer = restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl(this.integrationsUrl + this.egovPrefix)
                        .path("/get-egov-supplier-services")
                        .queryParam("supplierEIK", supplierEIK)
                        .toUriString(),
                LinkedHashMap.class);
        Object result = eServiceContainer.get("services");
        if(!result.equals("")){
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(result);
                    EServiceList eServiceList = objectMapper.readValue(bytes, EServiceList.class);
                    return eServiceList;
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new EServiceList();
    }

    @Override
    public EServiceDetails getServiceDetailsByNumber(List<String> serviceNumberList) {
        LinkedHashMap<Object,Object> result =  restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl(this.integrationsUrl + this.egovPrefix)
                        .path("/get-egov-supplier-service-details")
                        .queryParam("number", String.join(",", serviceNumberList))
                        .toUriString(),
                LinkedHashMap.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            EServiceDetails еServiceDetails = objectMapper.readValue(bytes, EServiceDetails.class);
            return еServiceDetails;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
