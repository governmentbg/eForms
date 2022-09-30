package com.bulpros.eforms.processengine.camunda.service;


import com.bulpros.eforms.processengine.camunda.model.UserProfileDto;
import com.bulpros.eforms.processengine.camunda.model.enums.AdditionalProfileRoleEnum;

import java.util.List;

public interface UserProfileService {
    UserProfileDto getUserProfileData(String projectId);

    List<String> getAdditionalProfileRolesByApplicant(UserProfileDto userProfile, String applicant);

    boolean hasUserRole(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum role);
}
