package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.exception.NotAllowedProfileType;
import com.bulpros.eformsgateway.form.web.controller.dto.ProfileTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.dto.ResourceDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationServiceServiceImpl implements ValidationServiceService {
    private final UserProfileService userProfileService;

    public void validateRequiredProfile(UserProfileDto userProfileDto, ResourceDto serviceDto, String applicant) {
        var allowedProfileTypes = (ArrayList<String>) serviceDto.getData().get("profileType");
        if (StringUtils.isEmpty(applicant)) {
            if (!allowedProfileTypes.contains(String.valueOf(ProfileTypeEnum.PERSON.getType()))) {
                throw new NotAllowedProfileType("Service: " + serviceDto.getData().get("arId") + " is not allowed for profile type person!");
            }
        } else {
            var profileAllowedToService = allowedProfileTypes.stream().filter(profile -> userProfileService.hasProfileType(userProfileDto, applicant,
                    ProfileTypeEnum.getByType(Integer.parseInt(profile)))).collect(Collectors.toList());
            if (profileAllowedToService.isEmpty())
                throw new NotAllowedProfileType("Service: " + serviceDto.getData().get("arId")
                        + " is not allowed for selected profile type!");
        }
    }
}
