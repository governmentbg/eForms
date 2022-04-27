package com.bulpros.eformsgateway.security.service;

import com.bulpros.eformsgateway.user.model.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    User getUser(Authentication authentication);
}
