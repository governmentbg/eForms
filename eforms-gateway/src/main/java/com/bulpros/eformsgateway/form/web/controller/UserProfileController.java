package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.exception.MissingUserPinException;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryProfileTypeEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;
import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;
import com.bulpros.eformsgateway.eformsintegrations.service.EGovUserAdministrationsAuthorizationService;
import com.bulpros.eformsgateway.eformsintegrations.service.EgovUserProfileService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.IdentifierTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.ResourceDataDto;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.model.User;
import com.bulpros.formio.security.FormioUserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserProfileController {

    private final EgovUserProfileService egovService;
    private final EGovUserAdministrationsAuthorizationService eGovUserAdministrationsAuthorizationService;
    private final UserProfileService userProfileService;
    private final FormioUserService userService;
    private final EgovUserProfileService egovUserProfileService;
    private final ModelMapper modelMapper;

    @Timed(value = "eforms-gateway-update-profile.time")
    @GetMapping(path = "projects/{projectId}/user-profile/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserProfileDto> userProfileUpdate(Authentication authentication, @PathVariable String projectId) {
        User user = userService.getUser(authentication);
        String personIdentifier = user.getPersonIdentifier();
        String personalIdentifierNumber;
        try {
            personalIdentifierNumber = personIdentifier.split("-")[1];
        } catch (Exception e) {
            throw new MissingUserPinException("MISSING_USER_PIN");
        }
        UserContactData egovUserProfile = this.egovService.getUserProfile(personalIdentifierNumber);
        UserContactData formioUserProfile = modelMapper.map(egovUserProfile, UserContactData.class);
        var updatedUserData = this.userProfileService.updateUserProfileData(projectId, authentication, formioUserProfile);
        return ResponseEntity.ok(updatedUserData);
    }

    @Timed(value = "eforms-gateway-update-additional-profiles.time")
    @GetMapping(path = "projects/{projectId}/user-profile/update-additional-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserProfileDto> userProfileUpdateAdditionalProfiles(Authentication authentication, @PathVariable String projectId) {
        User authUser = userService.getUser(authentication);

        String personIdentifier = authUser.getPersonIdentifier();
        String personalIdentifierNumber;
        try {
            personalIdentifierNumber = personIdentifier.split("-")[1];
        } catch (Exception e) {
            throw new MissingUserPinException("MISSING_USER_PIN");
        }

        var user = userProfileService.getUserProfileData(projectId, authentication);

        if (user == null) {
            user = getUserProfileDto(authentication, projectId, authUser);
        }
        if (user.getIsActive().equals(false)) {
            userProfileService.activateProfile(projectId, authentication);
        }

        UserAdministrationsAuthorization userAdministrationsAuthorization = this.eGovUserAdministrationsAuthorizationService.getUserAdministrationsAuthorization(personalIdentifierNumber);
        userAdministrationsAuthorization = addDefaultUserRole(userAdministrationsAuthorization);
        this.userProfileService.updateUserAdditionalProfiles(projectId, authentication, userAdministrationsAuthorization);
        var userProfile = userProfileService.getUserProfileData(projectId, authentication);
        return ResponseEntity.ok(userProfile);
    }

    @Timed(value = "eforms-gateway-projectId-user-profile.time")
    @GetMapping(path = "projects/{projectId}/user-profile", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserProfileDto> getUserProfileData(
            Authentication authentication, @PathVariable String projectId) {
        User authUser = userService.getUser(authentication);
        var user = userProfileService.getUserProfileData(projectId, authentication);

        if (user == null) {
            user = getUserProfileDto(authentication, projectId, authUser);
            return ResponseEntity.ok(user);
        }
        if (user.getIsActive().equals(false)) {
            user = userProfileService.activateProfile(projectId, authentication);
        }
        return ResponseEntity.ok(user);
    }

    @Timed(value = "eforms-gateway-decrypt-user-profile-id.time")
    @GetMapping(path = "projects/{projectId}/user-profile/profileId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> decryptUserProfileId(Authentication authentication, @PathVariable String projectId,
                                                @RequestParam String encryptProfileId) {
        String decryptUserProfileId = egovUserProfileService.decryptProfileId(encryptProfileId);
        if (StringUtils.isEmpty(decryptUserProfileId)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(decryptUserProfileId);
    }

    private UserProfileDto getUserProfileDto(Authentication authentication, String projectId, User authUser) {
        var userProfileDto = new UserProfileDto();
        userProfileDto.setPersonIdentifierType(IdentifierTypeEnum.EGN.getCode());
        userProfileDto.setPersonIdentifier(authUser.getPersonIdentifier());
        userProfileDto.setPersonName(authUser.getClaimAsString("name"));
        userProfileDto.setIsActive(true);
        ResourceDataDto<UserProfileDto> userProfileData = new ResourceDataDto<>();
        userProfileData.setData(userProfileDto);
        return userProfileService.createUserProfileData(projectId, authentication, userProfileData);
    }

    private UserAdministrationsAuthorization addDefaultUserRole(UserAdministrationsAuthorization userAdministrationsAuthorization) {
        var administrations = userAdministrationsAuthorization.getProfile().getAdministrations();
        if (administrations == null || administrations.isEmpty()) return userAdministrationsAuthorization;
        administrations.forEach(administration -> {
            if (administration.getProfileType().equals(EDeliveryProfileTypeEnum.INSTITUTION.getEGovValue())) {
                if (administration.getRoles() == null) {
                    var roles = new ArrayList<String>();
                    roles.add(AdditionalProfileRoleEnum.User.role);
                    administration.setRoles(roles);
                } else if (administration.getRoles().stream().noneMatch(role -> role.equalsIgnoreCase(AdditionalProfileRoleEnum.User.role))) {
                    administration.getRoles().add(AdditionalProfileRoleEnum.User.name());
                }
            }
        });
        return userAdministrationsAuthorization;
    }
}
