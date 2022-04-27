package com.bulpros.eformsgateway.eformsintegrations.repository;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EGovUserAdministrationsAuthorizationRepositoryImpl extends BaseRepository implements EGovUserAdministrationsAuthorizationRepository {

    public static final String GET_USER_ADMINISTRATIONS_AUTHORIZATION_CACHE = "getUserAdministrationsAuthorizationCache";

    @Override
    @Cacheable(value = GET_USER_ADMINISTRATIONS_AUTHORIZATION_CACHE, key = "#personalIdentifier", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier) {
        return getUserAdministrationsAuthorization(personalIdentifier, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_USER_ADMINISTRATIONS_AUTHORIZATION_CACHE, key = "#personalIdentifier", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier, String cacheControl) {
        try {
            RestTemplate restTemplate = null;
            UriComponentsBuilder request = UriComponentsBuilder.fromHttpUrl(getEGovUserAdministrationsAuthorizationUrl())
                    .queryParam("personalIdentifier", personalIdentifier);
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5 * 1000);
            requestFactory.setReadTimeout(5 * 1000);
            restTemplate = new RestTemplate(requestFactory);
            var responseEntity = restTemplate.getForEntity(request.toUriString(), UserAdministrationsAuthorization.class);
            return responseEntity.getBody();
        } catch (ResourceAccessException | HttpServerErrorException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
