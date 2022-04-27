package com.bulpros.eformsgateway.security.service;

import com.bulpros.eformsgateway.form.service.AssuranceLevelEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.user.model.User;

import java.util.Locale;

import static java.util.Objects.nonNull;

@Service
public class UserServiceImpl implements UserService {

    @Value("${keycloak.user.id.property}")
    private String keycloakUserIdProperty;

    @Value("${com.bulpros.token.claim.assurance-level}")
    private String claimAssuranceLevel;

    public User getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        User user = new User();
        if (principal instanceof Jwt) {
            Jwt jwtPrincipal = (Jwt) principal;
            user.setToken(jwtPrincipal.getTokenValue());
            user.setPersonIdentifier(jwtPrincipal.getClaimAsString(this.keycloakUserIdProperty));
            var assuranceLevel = jwtPrincipal.getClaimAsString(this.claimAssuranceLevel);
            if(nonNull(assuranceLevel)){
                user.setAssuranceLevel(AssuranceLevelEnum.valueOf(assuranceLevel.toUpperCase(Locale.ROOT)));
            }
            else {
                user.setAssuranceLevel(AssuranceLevelEnum.NONE);
            }

            user.setKeycloakId(jwtPrincipal.getClaimAsString("sub"));
            user.setGroups(jwtPrincipal.getClaimAsStringList("groups"));
            Integer ucnStartIndex = user.getPersonIdentifier().indexOf("-") + 1;
            if (ucnStartIndex != -1) {
                user.setUcn(user.getPersonIdentifier().substring(ucnStartIndex));
            }
        }

        return user;
    }
}
