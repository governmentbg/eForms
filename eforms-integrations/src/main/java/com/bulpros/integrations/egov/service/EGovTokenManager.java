package com.bulpros.integrations.egov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EGovTokenManager {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Value("${epdeau.registration.name}")
    private String epdeauClientRegistrationName;
    @Value("${spring.security.oauth2.client.registration.epdeau.client-id}")
    private String epdeauClientId;

    public String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest =
                OAuth2AuthorizeRequest.withClientRegistrationId(epdeauClientRegistrationName)
                        .principal(epdeauClientId)
                        .build();

        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientManager.authorize(authorizeRequest);

        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

        return accessToken.getTokenValue();
    }
}
