package com.bulpros.formio.security;

import com.bulpros.formio.model.User;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class FormioUserServiceImpl implements FormioUserService {

    @Value("${keycloak.user.id.property}")
    private String keycloakUserIdProperty;

    @Timed(value = "formio-get-user.time")
    @Override
    public User getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = new User();

        if(principal instanceof Jwt){
            Jwt jwtPrincipal = (Jwt) principal;
            user.setPersonIdentifier(jwtPrincipal.getClaimAsString(keycloakUserIdProperty));
            user.setKeycloakId(jwtPrincipal.getClaimAsString("sub"));
            user.setGroups(jwtPrincipal.getClaimAsStringList("groups"));
            Integer ucnStartIndex = user.getPersonIdentifier().indexOf("-") + 1;
            if (ucnStartIndex != -1) {
                user.setUcn(user.getPersonIdentifier().substring(ucnStartIndex));
            }
            user.setClaims(jwtPrincipal.getClaims());
        }
        return user;
    }
}
