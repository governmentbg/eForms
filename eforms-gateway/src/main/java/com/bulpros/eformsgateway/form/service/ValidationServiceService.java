package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.dto.ResourceDto;

public interface ValidationServiceService {
    void validateRequiredProfile(UserProfileDto userProfileDto, ResourceDto serviceDto, String applicant);
}
