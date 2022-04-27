package com.bulpros.eformsgateway.eformsintegrations.repository;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.eformsintegrations.model.PersonRegistrationResponse;

@Component
public class PersonRegistrationRepositoryImpl extends BaseRepository implements PersonRegistrationRepository {

    public static final String GET_PERSON_REGISTRATION_RESPONSE_CACHE = "getPersonRegistrationResponseCache";

    @Override
    @Cacheable(value = GET_PERSON_REGISTRATION_RESPONSE_CACHE, key = "#personIdentificator", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public PersonRegistrationResponse getPersonRegistrationResponse(String personIdentificator) {
        return getPersonRegistrationResponse(personIdentificator, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_PERSON_REGISTRATION_RESPONSE_CACHE, key = "#personIdentificator", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public PersonRegistrationResponse getPersonRegistrationResponse(String personIdentificator, String cacheControl) {
        var url = getUrlForPersonRegistrationCheck(personIdentificator);
        var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                PersonRegistrationResponse.class);
        return responseEntity.getBody();
    }
}
