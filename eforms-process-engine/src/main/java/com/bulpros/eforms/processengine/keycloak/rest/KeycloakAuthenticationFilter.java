package com.bulpros.eforms.processengine.keycloak.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.camunda.bpm.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.bulpros.eforms.processengine.security.IAuthenticationFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

/**
 * Keycloak Authentication Filter - used for REST API Security.
 */
@Slf4j
@RequiredArgsConstructor
public class KeycloakAuthenticationFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakAuthenticationFilter.class);

    private final IdentityService identityService;
    private final IAuthenticationFacade iAuthenticationFacade;
    private final OAuth2AuthorizedClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Extract user-name-attribute of the JWT / OAuth2 token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication instanceof JwtAuthenticationToken) {
            userId = ((JwtAuthenticationToken) authentication).getName();
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            userId = ((OidcUser) authentication.getPrincipal()).getName();
        } else {
            throw new ServletException("Invalid authentication request token");
        }
        if (userId.isEmpty()) {
            throw new ServletException("Unable to extract user-name-attribute from token");
        }

        LOG.debug("Extracted userId from bearer token: {}", userId);

        try {
            this.identityService.setAuthentication(userId, this.getUserRoles(authentication));
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.warn("Couldn't get principal authorities: " + e.getMessage());
        } finally {
            this.identityService.clearAuthentication();
        }
    }

    /**
     * Queries the groups of a given user.
     *
     * @param authentication the principal from Security context
     * @return list of groups the user belongs to
     */
    private List<String> getUserRoles(Authentication authentication) {
        List<String> roles = new ArrayList<>();
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            JSONArray rolesJson = (JSONArray) jwt.getClaims().get("roles");
            for (Object o : rolesJson) {
                roles.add(o.toString());
            }
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser OidcUser = (OidcUser) authentication.getPrincipal();
            Map<String, Object> claims = OidcUser.getClaims();
            if (claims != null && claims.containsKey("roles")) {
                roles = (List<String>) claims.get("roles");
            }
        }
        
        return roles;
    }
}
