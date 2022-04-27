package com.bulpros.eformsgateway.form.service;

import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_ACTIVE_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.CACHE_CONTROL_CONDITION;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.text.CaseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryProfileTypeEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;
import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorizationProfile;
import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;
import com.bulpros.eformsgateway.form.model.UserAdditionalProfile;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileDto;
import com.bulpros.eformsgateway.form.web.controller.dto.AdditionalProfileRoleEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.IdentifierTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.ProfileTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.ResourceDataDto;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.ResourceNotFoundException;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service(value = "userProfileServiceImpl")
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    
    public static final String GET_USER_PROFILE_DATA_CACHE = "getUserProfileDataCache";
    public static final String GET_USER_PROFILE_BY_NAME_CACHE = "getUserProfilesByNameCache";

    private final SubmissionService submissionService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    @Getter private final CacheService cacheService;
    private final ConfigurationProperties configurationProperties;

    private final static String NOT_AUTHORISED = "Not authorised";
    private final static String ACTIVE = "active";
    private final static String ACTIVE_STATUS = "active";
    private final static String INACTIVE_STATUS = "inactive";
    private final static String PUBLIC_INSTITUTION_PROFILE_TYPE = "4";
    private final static String INSTITUTION_PROFILE_TYPE = "3";

    @Override
    public UserProfileDto getUserProfileData(String projectId, Authentication authentication) {
        return getUserProfileData(projectId, authentication, PUBLIC_CACHE);
    }
    
    @Override
    public UserProfileDto getUserProfileData(String projectId, Authentication authentication, String cacheControl) {
        var user = this.userService.getUser(authentication);
        UserProfileDto cachedValue = cacheService.get(GET_USER_PROFILE_DATA_CACHE, user.getPersonIdentifier(), UserProfileDto.class, cacheControl);
        if (nonNull(cachedValue)) {
            // use cache
            return cachedValue;
        }
        
        ResourceDto userProfile = getUserProfile(projectId, authentication, configurationProperties.getUserProfilePath());
        List<ResourceDto> additionalUserProfile = getActiveAdditionalUserProfiles(projectId, authentication, configurationProperties.getAdditionalUserProfilePath());
        if (userProfile == null) return null;
        UserProfileDto userProfileDto = mergeUserProfileData(userProfile, additionalUserProfile);
        return cacheService.put(GET_USER_PROFILE_DATA_CACHE, user.getPersonIdentifier(), userProfileDto, cacheControl);
    }

    @Override
    public AdditionalProfileDto getAdditionalProfileByApplicant(UserProfileDto userProfile, String applicant) {
        if (applicant == null || applicant.isEmpty() || userProfile.getProfiles() == null) {
            return null;
        }
        Optional<AdditionalProfileDto> additionalProfile = userProfile.getProfiles()
                .stream()
                .filter(profile ->
                        applicant.equals(profile.getIdentifier()) &&
                                ACTIVE.equals(profile.getStatus()))
                .findFirst();
        return additionalProfile.orElse(null);
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

    @Override
    public boolean hasProfileType(UserProfileDto userProfileDto, String applicant, ProfileTypeEnum profileTypeEnum) {
        return userProfileDto.getProfiles()
                .stream()
                .filter(s -> s.getIdentifier().equals(applicant))
                .allMatch(profile -> profileTypeEnum.equals(
                        ProfileTypeEnum.getByType(
                                Integer.parseInt(profile.getProfileType())
                        )
                        )
                );
    }

    @Override
    @Cacheable(value = GET_USER_PROFILE_BY_NAME_CACHE, key = "#name", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public List<UserProfileDto> getUserProfilesByName(String projectId, Authentication authentication, String name) {
        return getUserProfilesByName(projectId, authentication, name, PUBLIC_CACHE);
    }
    
    @Override
    @Cacheable(value = GET_USER_PROFILE_BY_NAME_CACHE, key = "#name", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public List<UserProfileDto> getUserProfilesByName(String projectId, Authentication authentication, String name, String cacheControl) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.REGEX,
                Collections.singletonMap(configurationProperties.getUserNamePropertyKey(), "(.*)" + name + "(.*)")));
        var userProfileResources = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getUserprofileResourcePath()), authentication, filters);

        return userProfileResources
                .stream()
                .map(p -> modelMapper.map(p.getData(), UserProfileDto.class))
                .collect(Collectors.toList());
    }

    private List<ResourceDto> getUserAdditionalProfiles(String projectId, Authentication authentication, String userProfilePath) {
        var user = this.userService.getUser(authentication);
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), user.getPersonIdentifier())));
        var userList = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, userProfilePath), authentication, filters);
        if (userList.isEmpty()) return null;
        return userList;
    }

    private void createUserAdditionalProfile(String projectId, Authentication authentication, UserAdministrationsAuthorizationProfile.Administration administration, String personIdentifier) {
        String data = null;
        List<String> rolesToSync = Arrays.asList(configurationProperties.getRolesToSync().split("[, ]+").clone());
        ResourceDataDto<UserAdditionalProfile> userAdditionalProfileData = new ResourceDataDto<>();
        UserAdditionalProfile userAdditionalProfile = new UserAdditionalProfile();
        userAdditionalProfile.setPersonIdentifier(personIdentifier);
        if (administration.getProfileType().equals(PUBLIC_INSTITUTION_PROFILE_TYPE)) {
            userAdditionalProfile.setProfileType(INSTITUTION_PROFILE_TYPE);
        } else {
            userAdditionalProfile.setProfileType(administration.getProfileType());
        }
        userAdditionalProfile.setTitle(administration.getTitle());
        userAdditionalProfile.setIdentifierType(IdentifierTypeEnum.ORGANIZATION.getCode());
        userAdditionalProfile.setIdentifier(administration.getEik());
        userAdditionalProfile.setStatus(ACTIVE_STATUS);
        List<String> administrationRoles = rolesToLowerCamel(administration.getRoles());
        administrationRoles.retainAll(rolesToSync);
        userAdditionalProfile.setRoles(administrationRoles);
        userAdditionalProfileData.setData(userAdditionalProfile);

        try {
            data = objectMapper.writeValueAsString(userAdditionalProfileData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        submissionService.createSubmission(new ResourcePath(projectId, configurationProperties.getAdditionalUserProfilePath()),
                authentication, data);
    }

    private void deactivateUserAdditionalProfile(String projectId, Authentication authentication, UserAdditionalProfile profile) {
        JSONArray patchData = new JSONArray();
        JSONObject inactiveStatus = DataUtil.getJsonObjectForPatch("replace",
                configurationProperties.getAdditionalUserProfileStatus(), INACTIVE_STATUS);
        patchData.add(inactiveStatus);
        submissionService.updateSubmission(new ResourcePath(projectId, configurationProperties.getAdditionalUserProfilePath()), authentication, profile.getId(), patchData.toString());
    }

    private void activateUserAdditionalProfile(String projectId, Authentication authentication, UserAdditionalProfile profile) {
        JSONArray patchData = new JSONArray();
        JSONObject inactiveStatus = DataUtil.getJsonObjectForPatch("replace",
                configurationProperties.getAdditionalUserProfileStatus(), ACTIVE_STATUS);
        patchData.add(inactiveStatus);
        submissionService.updateSubmission(new ResourcePath(projectId, configurationProperties.getAdditionalUserProfilePath()), authentication, profile.getId(), patchData.toString());
    }

    private void syncUserAdditionalProfileRoles(String projectId, Authentication authentication, UserAdditionalProfile profile, List<UserAdministrationsAuthorizationProfile.Administration> administrations) {
        List<String> rolesToSync = Arrays.asList(configurationProperties.getRolesToSync().split("[, ]+").clone());
        UserAdministrationsAuthorizationProfile.Administration administration = administrations.stream().filter(a -> a.getEik().equals(profile.getIdentifier())).findFirst().get();
        List<String> administrationRoles = rolesToLowerCamel(administration.getRoles());
        administrationRoles.retainAll(rolesToSync);
        profile.getRoles().removeAll(rolesToSync);
        profile.getRoles().addAll(administrationRoles);

        JSONArray patchData = new JSONArray();
        JSONObject inactiveStatus = DataUtil.getJsonObjectForPatch("replace",
                configurationProperties.getAdditionalUserProfileRoles(), profile.getRoles());
        patchData.add(inactiveStatus);
        submissionService.updateSubmission(new ResourcePath(projectId, configurationProperties.getAdditionalUserProfilePath()), authentication, profile.getId(), patchData.toString());
    }

    @Override
    public Map<String, String> getUserIdToUserNameMap(String projectId, Authentication authentication, List<String> userIds) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.IN,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), userIds)));
        var userProfiles = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getUserprofileResourcePath()), authentication, filters);

        return userProfiles
                .stream()
                .collect(Collectors.toMap(
                        u -> (String) u.getData().get(configurationProperties.getUserIdPropertyKey()),
                        u -> (String) u.getData().get(configurationProperties.getUserNamePropertyKey())));
    }

    @Override
    public UserProfileDto createUserProfileData(String projectId, Authentication authentication, ResourceDataDto<UserProfileDto> userProfileDto) {
        String data = null;
        try {
            data = objectMapper.writeValueAsString(userProfileDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResourceDto userProfile = submissionService.createSubmission(
                new ResourcePath(projectId, configurationProperties.getUserProfilePath()), authentication, data);
        return modelMapper.map(userProfile.getData(), UserProfileDto.class);
    }

    @Override
    public UserProfileDto updateUserProfileData(String projectId, Authentication authentication, UserContactData userContactData) {
        return updateUserProfileData(projectId, authentication, userContactData, PUBLIC_CACHE);
    }

    @Override
    public UserProfileDto updateUserProfileData(String projectId, Authentication authentication, UserContactData userContactData, String cacheControl) {
        ResourceDto userProfile = getUserProfile(projectId, authentication, configurationProperties.getUserProfilePath());
        if (userProfile == null) throw new ResourceNotFoundException("USER_PROFILE_NOT_FOUND");
        var submissionId = userProfile.get_id();

        JSONArray patchData = new JSONArray();
        var ekatteValue = userContactData.getProfile().getAddress().getEkatte().getEkatte();
        if (NOT_AUTHORISED.equals(ekatteValue)) {
            JSONObject ekatteAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEkatteAuthorised(), false);
            patchData.add(ekatteAuthorised);
            JSONObject ekatteNumber = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEkatteNumber(), "");
            patchData.add(ekatteNumber);
            JSONObject districtCorrespondence = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getDistrictCorrespondence(), "");
            patchData.add(districtCorrespondence);
            JSONObject cityCorrespondence = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getCityCorrespondence(), "");
            patchData.add(cityCorrespondence);
            JSONObject municipalityCorrespondence = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getMunicipalityCorrespondence(), "");
            patchData.add(municipalityCorrespondence);
        } else {
            var municipalityValue = userContactData.getProfile().getAddress().getEkatte().getMunicipality();
            JSONObject municipality = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getMunicipalityCorrespondence(),
                    municipalityValue == null ? "" : municipalityValue);
            patchData.add(municipality);

            var regionValue = userContactData.getProfile().getAddress().getEkatte().getRegion();
            JSONObject region = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getDistrictCorrespondence(),
                    regionValue == null ? "" : regionValue);
            patchData.add(region);

            var populatedPlaceValue = userContactData.getProfile().getAddress().getEkatte().getPopulatedPlace();
            JSONObject populatedPlace = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getCityCorrespondence(),
                    populatedPlaceValue == null ? "" : populatedPlaceValue);
            patchData.add(populatedPlace);

            JSONObject ektee = DataUtil.getJsonObjectForPatch("replace", configurationProperties.getEkatteNumber(),
                    ekatteValue == null ? "" : ekatteValue);
            patchData.add(ektee);

            JSONObject ekatteAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEkatteAuthorised(), true);
            patchData.add(ekatteAuthorised);
        }

        var descriptionValue = userContactData.getProfile().getAddress().getDescription();
        if (NOT_AUTHORISED.equals(descriptionValue)) {
            JSONObject adrresslineCorrespondenceAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getAdrresslineCorrespondenceAuthorised(), false);
            patchData.add(adrresslineCorrespondenceAuthorised);
            JSONObject adrresslineCorrespondence = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getAdrresslineCorrespondence(), "");
            patchData.add(adrresslineCorrespondence);
        } else {
            JSONObject adrresslineCorrespondenceAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getAdrresslineCorrespondenceAuthorised(), true);
            patchData.add(adrresslineCorrespondenceAuthorised);
            JSONObject adrresslineCorrespondence = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getAdrresslineCorrespondence(),
                    descriptionValue == null ? "" : descriptionValue);
            patchData.add(adrresslineCorrespondence);
        }

        var phoneValue = userContactData.getProfile().getPhone();
        if (NOT_AUTHORISED.equals(phoneValue)) {
            JSONObject phoneAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getPhoneAuthorised(), false);
            patchData.add(phoneAuthorised);
            JSONObject phone = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getPhone(), "");
            patchData.add(phone);
        } else {
            JSONObject phoneAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getPhoneAuthorised(), true);
            patchData.add(phoneAuthorised);

            JSONObject phone = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getPhone(), phoneValue == null ? "" : phoneValue);
            patchData.add(phone);
        }

        var emailValue = userContactData.getProfile().getEmail();
        if (NOT_AUTHORISED.equals(emailValue)) {
            var emailAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEmailAuthorised(), false);
            patchData.add(emailAuthorised);
            var email = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEmail(), "");
            patchData.add(email);
        } else {
            var email = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEmail(), emailValue == null ? "" : emailValue);
            patchData.add(email);

            var emailAuthorised = DataUtil.getJsonObjectForPatch("replace",
                    configurationProperties.getEmailAuthorised(), true);
            patchData.add(emailAuthorised);
        }

        var userInfo = submissionService.updateSubmission(
                new ResourcePath(projectId, configurationProperties.getUserProfilePath()), authentication, submissionId, patchData.toString());
        List<ResourceDto> additionalUserProfile = getActiveAdditionalUserProfiles(projectId, authentication, configurationProperties.getAdditionalUserProfilePath());
        var userProfileData = mergeUserProfileData(userInfo, additionalUserProfile);
        return cacheService.putIfPresent(GET_USER_PROFILE_DATA_CACHE, userProfileData.getPersonIdentifier(), userProfileData, cacheControl);
    }

    @Override
    public UserProfileDto activateProfile(String projectId, Authentication authentication) {
        return activateProfile(projectId, authentication, PUBLIC_CACHE);
    }

    @Override
    public UserProfileDto activateProfile(String projectId, Authentication authentication, String cacheControl) {
        String userProfilePath = configurationProperties.getUserProfilePath();
        String additionalUserProfilePath = configurationProperties.getAdditionalUserProfilePath();
        ResourceDto userProfile = getUserProfile(projectId, authentication, userProfilePath);
        ResourceDto userInfo = null;
        JSONArray updateJsonArray = new JSONArray();
        updateJsonArray.add(DataUtil.getJsonObjectForPatch("replace", configurationProperties.getIsActive(), true));
        if (userProfile != null) {
            String submissionId = userProfile.get_id();
            userInfo = submissionService.updateSubmission(
                    new ResourcePath(projectId, userProfilePath),
                    authentication, submissionId, updateJsonArray.toString());
            List<ResourceDto> additionalUserProfile = getActiveAdditionalUserProfiles(projectId, authentication, additionalUserProfilePath);
            var userProfileData = mergeUserProfileData(userInfo, additionalUserProfile);
            return cacheService.putIfPresent(GET_USER_PROFILE_DATA_CACHE, userProfileData.getPersonIdentifier(), userProfileData, cacheControl);
        }
        return null;
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

    private List<ResourceDto> getActiveAdditionalUserProfiles(String projectId, Authentication authentication, String userProfilePath) {
        var user = this.userService.getUser(authentication);
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), user.getPersonIdentifier())));
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap("status", ACTIVE)));
        var userList = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, userProfilePath), authentication, filters);
        if (userList.isEmpty()) return null;
        return userList;
    }

    private ResourceDto getUserProfile(String projectId, Authentication authentication, String userProfilePath) {
        var user = this.userService.getUser(authentication);
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), user.getPersonIdentifier())));
        var userList = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, userProfilePath), authentication, filters);
        if (userList.isEmpty()) return null;
        return userList.get(0);
    }

    @Override
    public void updateUserAdditionalProfiles(String projectId, Authentication authentication, UserAdministrationsAuthorization userAdministrationsAuthorization) {
        var user = this.userService.getUser(authentication);
        String personIdentifier = user.getPersonIdentifier();
        List<ResourceDto> userAdditionalProfileSubmissions = getUserAdditionalProfiles(projectId, authentication, configurationProperties.getAdditionalUserProfilePath());
        List<UserAdditionalProfile> userAdditionalProfiles = new ArrayList<>();
        if (userAdditionalProfileSubmissions != null) {
            for (ResourceDto profileSubmission : userAdditionalProfileSubmissions) {
                UserAdditionalProfile userAdditionalProfile = modelMapper.map(profileSubmission.getData(), UserAdditionalProfile.class);
                userAdditionalProfile.setId(profileSubmission.get_id());
                userAdditionalProfiles.add(userAdditionalProfile);
            }
        }
        if (userAdministrationsAuthorization != null && userAdministrationsAuthorization.getResult().equals("success")) {
            List<UserAdditionalProfile> deactivateAdditionalProfiles = userAdditionalProfiles.stream()
                    .filter(p1 -> userAdministrationsAuthorization.getProfile().getAdministrations().stream()
                            .noneMatch(p2 -> p2.getEik().equals(p1.getIdentifier())))
                    .collect(Collectors.toList());

            List<UserAdditionalProfile> syncAdditionalProfiles = userAdditionalProfiles.stream()
                    .filter(p1 -> userAdministrationsAuthorization.getProfile().getAdministrations().stream()
                            .anyMatch(p2 -> p2.getEik().equals(p1.getIdentifier())))
                    .collect(Collectors.toList());

            List<UserAdministrationsAuthorizationProfile.Administration> createAdditionalProfiles = userAdministrationsAuthorization.getProfile().getAdministrations().stream()
                    .filter(p1 -> userAdditionalProfiles.stream()
                            .noneMatch(p2 -> p2.getIdentifier().equals(p1.getEik())))
                    .collect(Collectors.toList());

            deactivateAdditionalProfiles.stream().filter(f -> f.getStatus().equals(ACTIVE_STATUS)).forEach(p -> deactivateUserAdditionalProfile(projectId, authentication, p));
            syncAdditionalProfiles.stream().filter(f -> f.getStatus().equals(INACTIVE_STATUS)).forEach(p -> activateUserAdditionalProfile(projectId, authentication, p));
            syncAdditionalProfiles.stream().filter(f -> f.getStatus().equals(ACTIVE_STATUS)).forEach(p -> syncUserAdditionalProfileRoles(projectId, authentication, p, userAdministrationsAuthorization.getProfile().getAdministrations()));
            createAdditionalProfiles.forEach(p -> createUserAdditionalProfile(projectId, authentication, p, personIdentifier));

        } else {
            userAdditionalProfiles.stream().filter(f -> f.getStatus().equals(ACTIVE_STATUS)).forEach(p -> deactivateUserAdditionalProfile(projectId, authentication, p));
        }
        cacheService.evictIfPresent(GET_USER_PROFILE_DATA_CACHE, personIdentifier);
    }

    @Override
    public EDeliveryProfileTypeEnum getEDeliveryProfileTypeByApplicant(String projectId, String personIdentifier, Authentication authentication, String applicant) {
        List<SubmissionFilter> filters = new ArrayList<>();
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getUserIdPropertyKey(), personIdentifier)));
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getAdditionalProfileIdentifierPropertyKey(), applicant)));
        filters.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Collections.singletonMap(configurationProperties.getStatus(), ACTIVE)));
        var additionalProfiles = submissionService.getSubmissionsWithFilter(
                new ResourcePath(projectId, configurationProperties.getAdditionalUserProfilePath()), authentication, filters);
        if(additionalProfiles.isEmpty()) return null;
        return EDeliveryProfileTypeEnum.valueOfByEGovValue(additionalProfiles.get(0).getData().get("profileType").toString());
    }

    private List<String> rolesToLowerCamel(List<String> roles) {
        return roles.stream().map(r -> CaseUtils.toCamelCase(r, false, ' '))
                .collect(Collectors.toList());
    }
}