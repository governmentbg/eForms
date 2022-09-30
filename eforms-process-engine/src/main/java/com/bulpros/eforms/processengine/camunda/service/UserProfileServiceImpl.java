package com.bulpros.eforms.processengine.camunda.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bulpros.eforms.processengine.camunda.model.AdditionalProfileDto;
import com.bulpros.eforms.processengine.camunda.model.enums.AdditionalProfileRoleEnum;
import com.bulpros.eforms.processengine.camunda.model.UserProfileDto;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;

@Service(value = "userProfileServiceImpl")
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final SubmissionService submissionService;
    private final ModelMapper modelMapper;
    private final ConfigurationProperties configurationProperties;

    private final static String ACTIVE = "active";

    @Override
    public UserProfileDto getUserProfileData(String projectId) {

        ResourceDto userProfile = getUserProfile(projectId, configurationProperties.getUserProfilePath());
        List<ResourceDto> additionalUserProfile = getActiveAdditionalUserProfiles(projectId, configurationProperties.getAdditionalUserProfilePath());
        if (userProfile == null) return null;
        UserProfileDto userProfileDto = mergeUserProfileData(userProfile, additionalUserProfile);
        return userProfileDto;
    }

    @Override
    public List<String> getAdditionalProfileRolesByApplicant(UserProfileDto userProfile, String applicant) {
        if (applicant == null || applicant.isEmpty() || userProfile.getProfiles() == null) {
            return new ArrayList<>();
        }
        List<String> roles = new ArrayList<>();
        List<AdditionalProfileDto> additionalProfiles = userProfile.getProfiles()
                .stream()
                .filter(profile ->
                        applicant.equals(profile.getIdentifier()) &&
                                ACTIVE.equals(profile.getStatus()) &&
                                profile.getRoles() != null)
                .collect(Collectors.toList());
        for (AdditionalProfileDto additionalProfile : additionalProfiles) {
            roles.addAll(additionalProfile.getRoles());
        }
        return roles;
    }

    @Override
    public boolean hasUserRole(UserProfileDto userProfile, String applicant, AdditionalProfileRoleEnum role) {
        boolean result = false;
        if (applicant == null || applicant.isEmpty() ||
                userProfile.getProfiles() == null ||
                role == null || role.role.isEmpty()) {
            return result;
        }
        List<AdditionalProfileDto> additionalProfiles = userProfile.getProfiles()
                .stream()
                .filter(profile ->
                        applicant.equals(profile.getIdentifier()) &&
                                ACTIVE.equals(profile.getStatus()) &&
                                profile.getRoles() != null)
                .collect(Collectors.toList());
        for (AdditionalProfileDto additionalProfile : additionalProfiles) {
            result = result || additionalProfile.getRoles().stream().anyMatch(role.role::equalsIgnoreCase);
        }
        return result;
    }

    private UserProfileDto mergeUserProfileData(ResourceDto userProfile, List<ResourceDto> additionalUserProfile) {
        Map<String, Object> userInfo = userProfile.getData();
        List<AdditionalProfileDto> additionalProfiles = new ArrayList<>();
        if (additionalUserProfile != null) {
            for (ResourceDto profile : additionalUserProfile) {
                AdditionalProfileDto additionalProfile = modelMapper.map(profile.getData(), AdditionalProfileDto.class);
                additionalProfiles.add(additionalProfile);
            }
        }
        UserProfileDto userProfileDto = modelMapper.map(userInfo, UserProfileDto.class);
        userProfileDto.setProfiles(additionalProfiles);
        return userProfileDto;
    }

    private List<ResourceDto> getActiveAdditionalUserProfiles(String projectId, String userProfilePath) {
        var authentication = authenticationFacade.getAuthentication();
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), userService.getPrincipalIdentifier())));
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                Collections.singletonMap("status", ACTIVE)));
        var userList = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, userProfilePath), authentication, filters);
        if (userList.isEmpty()) return null;
        return userList;
    }

    private ResourceDto getUserProfile(String projectId, String userProfilePath) {
        var authentication = authenticationFacade.getAuthentication();
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), userService.getPrincipalIdentifier())));
        var userList = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, userProfilePath), authentication, filters);
        if (userList.isEmpty()) return null;
        return userList.get(0);
    }
}
