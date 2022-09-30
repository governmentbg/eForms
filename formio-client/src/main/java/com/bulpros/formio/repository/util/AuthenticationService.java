package com.bulpros.formio.repository.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AuthenticationService {
    @Value("${keycloak.user.id.property}")
    private String keycloakUserIdProperty;
    @Value("${com.bulpros.process-admin.group}")
    private String processAdminGroupId;

    private static String KEYCLOAK_USER_ID_PROPERTY;
    private static  String PROCESS_ADMIN_GROUP_ID;

    @Value("${keycloak.user.id.property}")
    public void setKeycloakUserIdProperty(String keycloakUserIdProperty){
        AuthenticationService.KEYCLOAK_USER_ID_PROPERTY = keycloakUserIdProperty;
    }

    @Value("${com.bulpros.process-admin.group}")
    public void setProcessAdminGroupId(String processAdminGroupId){
        AuthenticationService.PROCESS_ADMIN_GROUP_ID = processAdminGroupId;
    }

    public static Authentication createServiceAuthentication() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "RS256");
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "service");
        claims.put(AuthenticationService.KEYCLOAK_USER_ID_PROPERTY, "PNOBG-0000000000");
        List<String> groups = new ArrayList<>();
        groups.add(AuthenticationService.PROCESS_ADMIN_GROUP_ID);
        claims.put("formio_groups", groups);
        claims.put("given_name", "service");
        claims.put("family_name", "service");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("service");
        authorities.add(authority);
        Jwt jwt = new Jwt("key", Instant.now(), Instant.now().plus(1l, ChronoUnit.HOURS), headers, claims);
        Authentication authentication = new AnonymousAuthenticationToken("key", jwt, authorities);
        return authentication;
    }
}
