package com.bulpros.eforms.processengine.security;

public interface UserService {

    AssuranceLevelEnum getPrincipalAssuranceLevel();
    String getPrincipalIdentifier();
}
