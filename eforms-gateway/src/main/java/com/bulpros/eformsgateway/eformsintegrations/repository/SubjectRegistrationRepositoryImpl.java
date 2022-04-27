package com.bulpros.eformsgateway.eformsintegrations.repository;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.bulpros.eformsgateway.eformsintegrations.model.SubjectRegistrationResponse;

@Component
public class SubjectRegistrationRepositoryImpl extends BaseRepository implements SubjectRegistrationRepository {

    public static final String GET_SUBJECT_REGISTRATION_CACHE = "getSubjectRegistrationCache";

    @Override
    @Cacheable(value = GET_SUBJECT_REGISTRATION_CACHE, key = "#identifier", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public SubjectRegistrationResponse getSubjectRegistrationResponse(String identifier) {
        return getSubjectRegistrationResponse(identifier, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_SUBJECT_REGISTRATION_CACHE, key = "#identifier", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public SubjectRegistrationResponse getSubjectRegistrationResponse(String identifier, String cacheControl) {
        var url = getUrlForSubjectRegistrationCheck(identifier);
        var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                SubjectRegistrationResponse.class);
        return responseEntity.getBody();
    }
}
