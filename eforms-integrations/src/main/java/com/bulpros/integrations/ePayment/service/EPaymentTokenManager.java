package com.bulpros.integrations.ePayment.service;

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
public class EPaymentTokenManager {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Value("${epayment.registration.name}")
    private String ePaymentClientRegistrationName;
    @Value("${spring.security.oauth2.client.registration.epayment.client-id}")
    private String ePaymentClientId;

    public String getAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest =
                OAuth2AuthorizeRequest.withClientRegistrationId(ePaymentClientRegistrationName)
                        .principal(ePaymentClientId)
                        .build();

        OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientManager.authorize(authorizeRequest);

        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

        return accessToken.getTokenValue();
    }
}
