package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.egov.model.supplier.SupplierDetailsInfo;
import com.bulpros.eforms.processengine.egov.model.supplier.SupplierList;
import com.bulpros.eforms.processengine.egov.model.supplier.Suppliers;
import com.bulpros.eforms.processengine.egov.model.supplier.SuppliersDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final RestTemplate restTemplate;
    private final ConfigurationProperties configurationProperties;

    @Override
    public Suppliers getAllSuppliers() {
        return restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl( configurationProperties.getIntegrationsUrl() +
                       configurationProperties.getEgovPrefix())
                        .path("/get-egov-suppliers")
                        .toUriString(),
                Suppliers.class);
    }

    @Override
    public SupplierDetailsInfo getSupplierDetails(String code) {
        SuppliersDetails suppliers = restTemplate.getForObject(
                UriComponentsBuilder.fromHttpUrl(configurationProperties.getIntegrationsUrl() + configurationProperties.getEgovPrefix())
                    .path("/get-egov-supplier-details")
                    .queryParam("code", code)
                    .toUriString(),
                SuppliersDetails.class);
        return suppliers.getSuppliers().getSupplier();
    }
}
