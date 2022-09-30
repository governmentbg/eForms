package com.bulpros.integrations.configuration.esb;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2ClientCredentialsGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class EsbGrantRequestEntityConverter
        implements Converter<OAuth2ClientCredentialsGrantRequest, RequestEntity<?>> {

    /**
     * Returns the {@link RequestEntity} used for the Access Token Request.
     *
     * @param clientCredentialsGrantRequest the client credentials grant request
     * @return the {@link RequestEntity} used for the Access Token Request
     */
    @Override
    public RequestEntity<?> convert(OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest) {
        ClientRegistration clientRegistration = clientCredentialsGrantRequest.getClientRegistration();
        HttpHeaders headers = getDefaultTokenRequestHeaders();
        MultiValueMap<String, String> formParameters = this.buildFormParameters(clientCredentialsGrantRequest);
        URI uri = UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getTokenUri()).build()
                .toUri();
        return new RequestEntity<>(formParameters, headers, HttpMethod.POST, uri);
    }

    /**
     * Returns a {@link MultiValueMap} of the form parameters used for the Access Token
     * Request body.
     *
     * @param clientCredentialsGrantRequest the client credentials grant request
     * @return a {@link MultiValueMap} of the form parameters used for the Access Token
     * Request body
     */
    private MultiValueMap<String, String> buildFormParameters(
            OAuth2ClientCredentialsGrantRequest clientCredentialsGrantRequest) {
        ClientRegistration clientRegistration = clientCredentialsGrantRequest.getClientRegistration();
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, clientCredentialsGrantRequest.getGrantType().getValue());
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        if (!CollectionUtils.isEmpty(clientRegistration.getScopes())) {
            formParameters.add(OAuth2ParameterNames.SCOPE,
                    StringUtils.collectionToDelimitedString(clientRegistration.getScopes(), " "));
        }
        return formParameters;
    }

    private static HttpHeaders getDefaultTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        final MediaType contentType = MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setContentType(contentType);
        return headers;
    }

}
