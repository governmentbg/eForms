package com.bulpros.eformsgateway.form.service;


import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryProfileTypeEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;
import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

public interface UserProfileService {
    UserProfileDto getUserProfileData(String projectId, Authentication authentication);
    UserProfileDto getUserProfileData(String projectId, Authentication authentication, String cacheControl);
    List<String> getAdditionalProfileRolesByApplicant(UserProfileDto userProfile, String applicant);
    AdditionalProfileDto getAdditionalProfileByApplicant(UserProfileDto userProfile, String applicant);
    boolean hasUserRole(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum role);
    boolean hasProfileType(UserProfileDto userProfileDto, String applicant, ProfileTypeEnum profileTypeEnum);
    List<UserProfileDto> getUserProfilesByName(String projectId, Authentication authentication, String name);
    List<UserProfileDto> getUserProfilesByName(String projectId, Authentication authentication, String name, String cacheControl);
    Map<String, String> getUserIdToUserNameMap(String projectId, Authentication authentication, List<String> userIds);
    UserProfileDto createUserProfileData(String projectId, Authentication authentication, ResourceDataDto<UserProfileDto> userProfileDto);
    UserProfileDto updateUserProfileData(String projectId, Authentication authentication, UserContactData userData);
    UserProfileDto updateUserProfileData(String projectId, Authentication authentication, UserContactData userData, String cacheControl);
    UserProfileDto activateProfile(String projectId, Authentication authentication);
    UserProfileDto activateProfile(String projectId, Authentication authentication, String cacheControl);
    void updateUserAdditionalProfiles(String projectId, Authentication authentication, UserAdministrationsAuthorization userAdministrationsAuthorization);
    EDeliveryProfileTypeEnum getEDeliveryProfileTypeByApplicant(String projectId, String personIdentifier, Authentication authentication, String applicant);
}
