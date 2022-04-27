package com.bulpros.eforms.processengine.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;

import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.security.oauth2.client.provider.keycloak.user-name-attribute}")
    private String userNameAttribute;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public AssuranceLevelEnum getPrincipalAssuranceLevel() {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        String assuranceLevelString = null;
        if (authentication.getPrincipal() instanceof Jwt) {
            assuranceLevelString =  ((Jwt) authentication.getPrincipal()).getClaimAsString(ProcessConstants.PRINCIPAL_ASSURANCE_LEVEL);
        }
        if(assuranceLevelString == null) return AssuranceLevelEnum.NONE;
        return AssuranceLevelEnum.valueOf(assuranceLevelString.toUpperCase(Locale.ROOT));
    }

    @Override
    public String getPrincipalIdentifier() {
        switch (this.userNameAttribute) {
        case "sub":
            return this.getPricipalId();
        case "preferred_username":
            return this.getPrincipalPreferredName();
        case "personIdentifier":
            return this.getPersonIdentifier();
        default:
            return null;
        }
    }

    private String getPrincipalPreferredName() {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            return ((Jwt) authentication.getPrincipal()).getClaimAsString(ProcessConstants.PREFERRED_USERNAME);
        }
        return null;
    }

    private String getPersonIdentifier() {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            return ((Jwt) authentication.getPrincipal()).getClaimAsString(ProcessConstants.PERSON_IDENTIFIER);
        }
        return null;
    }

    private String getPricipalId() {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        if (authentication.getPrincipal() instanceof Jwt) {
            return ((Jwt) authentication.getPrincipal()).getClaimAsString(ProcessConstants.SUB);
        }
        return null;
    }

}
