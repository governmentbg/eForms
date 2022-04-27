package com.bulpros.eformsgateway.eformsintegrations.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bulpros.eformsgateway.cache.service.CacheService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseRepository {

    protected static final String SLASH_SYMBOL = "/";

    @Autowired
    protected IntegrationsConfigurationProperties integrationsConfProperties;
    @Autowired
    protected RestTemplate restTemplate;
    
    // Do not removed: Used in @Cacheable condition SpEL expression
    @Autowired @Getter
    private CacheService cacheService;

    protected String getOrnUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(
                integrationsConfProperties.getIntegrationsUrl())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getIntegrationsRootPath())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getOrnResourcePath())
                .path(SLASH_SYMBOL);

        return url.toUriString();
    }

    protected String getEgovUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(integrationsConfProperties.getIntegrationsUrl())
            .path(SLASH_SYMBOL)
            .path(integrationsConfProperties.getIntegrationsRootPath())
            .path(SLASH_SYMBOL)
            .path(integrationsConfProperties.getEgovResourcePath())
            .path(SLASH_SYMBOL)
            .path("get-egov-user-contact-data");

        return url.toUriString();
    }

    protected String getEGovUserAdministrationsAuthorizationUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(integrationsConfProperties.getIntegrationsUrl())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getIntegrationsRootPath())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getEgovResourcePath())
                .path(SLASH_SYMBOL)
                .path("get-egov-user-administrations-authorization");

        return url.toUriString();
    }

    protected String getUrlForSubjectRegistrationCheck(String identifier) {
        var url = UriComponentsBuilder.fromHttpUrl(integrationsConfProperties.getIntegrationsUrl())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getIntegrationsRootPath())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getEDeliveryResourcePath())
                .path("/check-subject-has-registration")
                .queryParam("identifier", identifier);
        return url.toUriString();
    }

    protected String getUrlForLegalPersonRegistrationCheck(String eik) {
        var url = UriComponentsBuilder.fromHttpUrl(integrationsConfProperties.getIntegrationsUrl())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getIntegrationsRootPath())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getEDeliveryResourcePath())
                .path("/check-legal-person-has-registration")
                .queryParam("identificator", eik);
        return url.toUriString();
    }

    protected String getUrlForPersonRegistrationCheck(String personIdentificator) {
        var url = UriComponentsBuilder.fromHttpUrl(integrationsConfProperties.getIntegrationsUrl())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getIntegrationsRootPath())
                .path(SLASH_SYMBOL)
                .path(integrationsConfProperties.getEDeliveryResourcePath())
                .path("/check-person-has-registration")
                .queryParam("person-identificator", personIdentificator);
        return url.toUriString();
    }
}
