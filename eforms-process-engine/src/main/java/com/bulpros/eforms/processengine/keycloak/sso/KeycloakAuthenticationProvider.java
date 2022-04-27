package com.bulpros.eforms.processengine.keycloak.sso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.AuthenticationResult;
import org.camunda.bpm.engine.rest.security.auth.impl.ContainerBasedAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.StringUtils;

/**
 * OAuth2 Authentication Provider for usage with Keycloak and
 * KeycloakIdentityProviderPlugin.
 */
public class KeycloakAuthenticationProvider extends ContainerBasedAuthenticationProvider {

    @Override
    public AuthenticationResult extractAuthenticatedUser(HttpServletRequest request, ProcessEngine engine) {

        // Extract user-name-attribute of the OAuth2 token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken)
                || !(authentication.getPrincipal() instanceof OidcUser)) {
            return AuthenticationResult.unsuccessful();
        }
        String userId = ((OidcUser) authentication.getPrincipal()).getName();
        if (!StringUtils.hasLength(userId)) {
            return AuthenticationResult.unsuccessful();
        }

        // Authentication successful
        AuthenticationResult authenticationResult = new AuthenticationResult(userId, true);
        authenticationResult.setGroups(this.getUserRoles(authentication));

        return authenticationResult;
    }

    private List<String> getUserRoles(Authentication authentication) {
        List<String> roles = new ArrayList<>();
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        Map<String, Object> claims = oidcUser.getClaims();
        if (claims != null && claims.containsKey("roles")) {
            roles = (List<String>) claims.get("roles");
        }
        return roles;
    }

}