package com.bulpros.integrations.egov.service;

import com.bulpros.integrations.egov.model.UserContactDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component("eGovService")
@RequiredArgsConstructor
public class EGovService {

    private final RestTemplate restTemplateEsb;
    private final EGovTokenManager tokenManager;

    @Value("${com.bulpros.egov.user.contact.data.url}")
    private String eGovUrl;
    @Value("${com.bulpros.egov.user.administrations.authorization.url}")
    private String eGovUserAdministrationsAuthorizationUrl;
    @Value("${com.bulpros.egov.suppliers.url}")
    private String eGovSuppliersUrl;
    @Value("${com.bulpros.egov.supplier.details.url}")
    private String eGovSupplierDetailsUrl;
    @Value("${com.bulpros.egov.supplier.services.url}")
    private String eGovSupplierServicesUrl;
    @Value("${com.bulpros.egov.supplier.service.details.url}")
    private String eGovSupplierServiceDetailsUrl;

    public UserContactDataResponse getEGovUserContactData(String identifier) throws Exception {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovUrl)
                .queryParam("identifier", identifier);

        ResponseEntity<UserContactDataResponse> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(),
                        UserContactDataResponse.class);
        return responseEntity.getBody();
    }

    public Map<Object, Object> getEGovUserAdministrationsAuthorization(String personalIdentifier) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovUserAdministrationsAuthorizationUrl)
                .queryParam("format", "json")
                .queryParam("personalIdentifier", personalIdentifier);

        ResponseEntity<Map<Object, Object>> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(), new ParameterizedTypeReference<Map<Object, Object>>() {
                });
        return responseEntity.getBody();
    }

    public Map<Object, Object> getEGovSuppliers() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovSuppliersUrl)
                .queryParam("format", "json");

        ResponseEntity<Map<Object, Object>> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(), new ParameterizedTypeReference<Map<Object, Object>>() {
                });
        return responseEntity.getBody();
    }

    public Map<Object, Object> getEGovSupplierDetails(String code) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovSupplierDetailsUrl)
                .queryParam("format", "json")
                .queryParam("code", code);

        ResponseEntity<Map<Object, Object>> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(), new ParameterizedTypeReference<Map<Object, Object>>() {
                });
        return responseEntity.getBody();
    }

    public Map<Object, Object> getEGovSupplierServices(String supplierEIK) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovSupplierServicesUrl)
                .queryParam("format", "json")
                .queryParam("supplierEIK", supplierEIK);

        ResponseEntity<Map<Object, Object>> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(), new ParameterizedTypeReference<Map<Object, Object>>() {
                });
        return responseEntity.getBody();
    }

    public Map<Object, Object> getEGovSupplierServiceDetails(String number) {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(this.eGovSupplierServiceDetailsUrl)
                .queryParam("format", "json")
                .queryParam("number", number);

        ResponseEntity<Map<Object, Object>> responseEntity =
                this.restTemplateEsb.exchange(url.toUriString(), HttpMethod.GET, prepareRequest(), new ParameterizedTypeReference<Map<Object, Object>>() {
                });
        return responseEntity.getBody();
    }

    private HttpEntity<Void> prepareRequest() {
        String token = tokenManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
