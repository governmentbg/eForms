package com.bulpros.eformsgateway.form.service;

import static com.bulpros.eformsgateway.form.service.UserProfileServiceImpl.GET_USER_PROFILE_DATA_CACHE;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bulpros.formio.utils.Page;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.web.controller.dto.FullAdditionalProfileDto;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;

@Service
@RequiredArgsConstructor
public class AdditionalProfileServiceImpl implements AdditionalProfileService {
    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;
    private final ModelMapper modelMapper;
    private final CacheService cacheService;

    @Override
    public Page<ResourceDto> getAdditionalProfiles(String projectId, Authentication authentication,
                                                                UserProfileDto userProfile, String applicant,
                                                                Long page, Long size, String sort) {
        var fullAdditionalProfiles = getFullAdditionalProfiles(projectId, authentication,
                applicant, page, size, sort);

        var userProfiles = getUserProfiles(projectId, authentication, fullAdditionalProfiles);

        fullAdditionalProfiles.getElements()
                .forEach(profile -> profile.getData().put("name",
                        (userProfiles.stream()
                                .filter(userProfileDto ->
                                        userProfileDto.getPersonIdentifier()
                                                .equals(profile.getData().get("personIdentifier")))
                                .findFirst()
                                .get()
                                .getPersonName()
                        )
                ));
        return fullAdditionalProfiles;
    }

    @Override
    public void updateAdditionalProfile(String projectId, Authentication authentication, FullAdditionalProfileDto additionalProfile) {
        var resourcePath = new ResourcePath(projectId, configuration.getAdditionalUserProfilePath());
        String additionalProfileId = getProfileId(authentication, additionalProfile, resourcePath);
        var patchData =  new JSONArray();
        patchData.add(DataUtil.getJsonObjectForPatch(
                "replace",
                configuration.getAdditionalUserProfileRoles(),
                additionalProfile.getRoles()
        ));

        submissionService.updateSubmission(
                resourcePath,
                authentication,
                additionalProfileId,
                patchData.toString()
        );
        cacheService.evictIfPresent(GET_USER_PROFILE_DATA_CACHE, additionalProfile.getPersonIdentifier());
    }

    private List<UserProfileDto> getUserProfiles(String projectId, Authentication authentication,
                                                 Page<ResourceDto> fullAdditionalProfiles) {
        var pinFilter = List.of(
                new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Collections.singletonMap(
                                configuration.getUserIdPropertyKey(),
                                fullAdditionalProfiles.getElements().stream()
                                        .map(resourceDto -> resourceDto.getData().get("personIdentifier"))
                                        .collect(Collectors.toList()))
                )
        );

        var profiles =  submissionService.getAllSubmissionsWithFilter(
                        new ResourcePath(projectId, configuration.getUserprofileResourcePath()),
                        authentication,
                        pinFilter,100L, true);
        return profiles.stream()
                .map((Function<ResourceDto, Object>) ResourceDto::getData)
                .map(data -> modelMapper.map(data, UserProfileDto.class))
                .collect(Collectors.toList());
    }

    private Page<ResourceDto> getFullAdditionalProfiles(String projectId, Authentication authentication,
                                                                     String applicant, Long page, Long size, String sort) {
        var filters = List.of(
                new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Collections.singletonMap(configuration.getAdditionalProfileIdentifierPropertyKey(), applicant)
                ));
        return submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configuration.getAdditionalUserProfilePath()),
                authentication,
                filters, page, size, sort );
    }

    private String getProfileId(Authentication authentication, FullAdditionalProfileDto additionalProfile, ResourcePath resourcePath) {
        var profileFilter = List.of(new SubmissionFilter(
                        SubmissionFilterClauseEnum.IN,
                        Map.of(
                                configuration.getAdditionalProfileIdentifierPropertyKey(), additionalProfile.getIdentifier(),
                                configuration.getUserIdPropertyKey(), additionalProfile.getPersonIdentifier()
                        )
                )
        );
        var submissions = submissionService.getSubmissionsWithFilter(resourcePath, authentication, profileFilter);
        if (submissions == null || submissions.isEmpty()) {
            throw new ResourceNotFoundException(String.format("No profiles with identifier = %s and personalIndetifier = %s found.",
                    additionalProfile.getIdentifier(), additionalProfile.getPersonIdentifier()));
        }
        return submissions
                .get(0)
                .get_id();
    }
}
