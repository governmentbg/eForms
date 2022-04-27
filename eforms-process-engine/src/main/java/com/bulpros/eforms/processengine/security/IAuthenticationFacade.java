package com.bulpros.eforms.processengine.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();
}
