package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.form.service.AdditionalProfileService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.FullAdditionalProfileDto;
import com.bulpros.eformsgateway.form.web.controller.dto.ProfileTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import static com.bulpros.eformsgateway.form.utils.ValidationUtils.isNotPresent;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAdditionalProfileController {

    private final UserProfileService userProfileService;
    private final AdditionalProfileService additionalProfileService;

    @Timed(value = "eforms-gateway-admin-get_additional-profile-data.time")
    @GetMapping(path = "projects/{projectId}/user-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getAdditionalProfileData(Authentication authentication,
                                                               @RequestParam String applicant,
                                                               @PathVariable String projectId,
                                                               @RequestParam(required = false) Long page,
                                                               @RequestParam(required = false) Long size,
                                                               @RequestParam(required = false) String sort) {
        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (isNotValidRequest(applicant, userProfile)) {
            return getForbiddenListResponse();
        }
        if (userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.Admin)) {
            return ResponseEntity.ok(additionalProfileService.getAdditionalProfiles(projectId, authentication,
                    userProfile, applicant, page, size, sort));
        }
        return getForbiddenListResponse();
    }

    @Timed(value = "eforms-gateway-admin-update-additional-profile-data.time")
    @PutMapping(path = "projects/{projectId}/user-profiles", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> updateAdditionalProfileData(Authentication authentication,
                                                                         @RequestBody FullAdditionalProfileDto profile,
                                                                         @RequestParam String applicant,
                                                                         @PathVariable String projectId) {

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (isNotValidRequest(applicant, userProfile)) {
            return getForbiddenResponse();
        }
        if (userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.Admin)) {
            additionalProfileService.updateAdditionalProfile(projectId, authentication, profile);
            return ResponseEntity.accepted().build();
        }
        return getForbiddenResponse();
    }

    private boolean isNotValidRequest(String applicant, UserProfileDto userProfile) {
        return isNotPresent(applicant) && !userProfileService.hasProfileType(userProfile, applicant, ProfileTypeEnum.INSTITUTION);
    }

    private ResponseEntity<Page<ResourceDto>> getForbiddenListResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Void> getForbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
