package com.bulpros.eformsgateway.eformsintegrations.repository;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;

@Component
public class EgovUserProfileRepositoryImpl extends BaseRepository implements EgovUserProfileRepository {

    public static final String GET_USER_PROFILE_CACHE = "getUserProfileCache";

    @Override 
    @Cacheable(value = GET_USER_PROFILE_CACHE, key = "#personalIdentifierNumber", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public UserContactData getUserProfile(String personalIdentifierNumber) {
        return getUserProfile(personalIdentifierNumber, PUBLIC_CACHE);
    }
    
    @Override 
    @Cacheable(value = GET_USER_PROFILE_CACHE, key = "#personalIdentifierNumber", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public UserContactData getUserProfile(String personalIdentifierNumber, String cacheControl) {
        UriComponentsBuilder request = UriComponentsBuilder.fromHttpUrl(getEgovUrl())
            .queryParam("identifier", personalIdentifierNumber);
        var responseEntity = restTemplate.getForEntity(request.toUriString(), UserContactData.class);
        return responseEntity.getBody();
    }
}
