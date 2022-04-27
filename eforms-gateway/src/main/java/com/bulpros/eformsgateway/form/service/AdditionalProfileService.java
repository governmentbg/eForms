package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.FullAdditionalProfileDto;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AdditionalProfileService {

    Page<ResourceDto> getAdditionalProfiles(String projectId, Authentication authentication, UserProfileDto userProfile,
                                            String applicant, Long page, Long size, String sort);
    void updateAdditionalProfile(String projectId, Authentication authentication, FullAdditionalProfileDto additionalProfile);
}
