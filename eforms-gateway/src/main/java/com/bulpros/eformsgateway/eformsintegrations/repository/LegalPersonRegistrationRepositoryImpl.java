package com.bulpros.eformsgateway.eformsintegrations.repository;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.eformsintegrations.model.LegalPersonRegistrationResponse;

@Component
public class LegalPersonRegistrationRepositoryImpl extends BaseRepository implements LegalPersonRegistrationRepository {

    public static final String GET_LEGAL_PERSON_REGISTRATION_RESPONSE_CACHE = "getLegalPersonRegistrationResponseCache";

    @Override
    @Cacheable(value = GET_LEGAL_PERSON_REGISTRATION_RESPONSE_CACHE, key = "#eik", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public LegalPersonRegistrationResponse getLegalPersonRegistrationResponse(String eik) {
        return getLegalPersonRegistrationResponse(eik, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_LEGAL_PERSON_REGISTRATION_RESPONSE_CACHE, key = "#eik", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public LegalPersonRegistrationResponse getLegalPersonRegistrationResponse(String eik, String cacheControl) {
        var url = getUrlForLegalPersonRegistrationCheck(eik);
        var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                LegalPersonRegistrationResponse.class);
        return responseEntity.getBody();
    }
}
