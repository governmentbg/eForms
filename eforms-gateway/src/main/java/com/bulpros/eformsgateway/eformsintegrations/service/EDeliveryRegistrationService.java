package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.user.model.User;
import org.springframework.security.core.Authentication;

public interface EDeliveryRegistrationService {

    CheckEDeliveryRegistrationResult checkRegistration(User user, Authentication authentication, String projectId, String applicant);
}
