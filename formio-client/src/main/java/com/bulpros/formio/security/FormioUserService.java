package com.bulpros.formio.security;

import com.bulpros.formio.model.User;
import org.springframework.security.core.Authentication;

public interface FormioUserService {
    User getUser(Authentication authentication);
}
