package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.eformsintegrations.model.ETranslationRequest;
import com.bulpros.eformsgateway.eformsintegrations.service.ETranslationService;
import com.bulpros.eformsgateway.form.exception.StoreDataException;
import com.bulpros.eformsgateway.form.exception.TranslationFailed;
import com.bulpros.eformsgateway.form.model.TranslationRequest;
import com.bulpros.eformsgateway.form.service.*;
import com.bulpros.eformsgateway.form.utils.CommonUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.utils.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminTranslationController {

    private final LanguagesService languagesService;
    private final LanguageTranslationsService languageTranslationsService;
    private final TranslationRequestService translationRequestService;
    private final ETranslationService eTranslationService;
    private final UserProfileService userProfileService;
    private final SubmissionService submissionService;
    private final SubmissionPatchService submissionPatchService;
    private final ConfigurationProperties configuration;
    private final CacheService cacheService;

    @PostMapping(value = "/projects/{projectId}/translation/issue-translation/{targetLanguageCode}/{key}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDto> issueTranslationRequest(Authentication authentication,
                                                              @RequestParam String applicant,
                                                              @PathVariable String projectId,
                                                              @PathVariable String targetLanguageCode,
                                                              @PathVariable String key) {
        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponse();

        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId,authentication, targetLanguageCode);
        if(languageData == null){
            throw new TranslationFailed("Unrecognized language: " + targetLanguageCode);
        }

        ResourceDto translationData = languageTranslationsService.getLanguageTranslationsByDefaultIsoCodeAndKey(projectId,authentication,key);
        if(translationData == null){
             throw new TranslationFailed("No translation in default language for key: " + key);
        }

        int requestId = 0;
        ETranslationRequest eTranslationRequest = new ETranslationRequest();
        eTranslationRequest.setExternalReference(key);
        eTranslationRequest.setTargetLanguages(List.of(languageData.getData().get(configuration.getLanguage()).toString()));
        eTranslationRequest.setSourceLanguage(configuration.getDefaultLanguageCode());
        eTranslationRequest.setTextToTranslate(translationData.getData().get(configuration.getTranslation()).toString());
        eTranslationRequest.setRequesterCallback(configuration.getRequesterCallback());
        eTranslationRequest.setErrorCallback(configuration.getErrorCallback());

        requestId = eTranslationService.sendTranslationRequest(eTranslationRequest);

        TranslationRequest translationRequest = new TranslationRequest();
        translationRequest.setExternalReference(key);
        translationRequest.setTargetLanguageCode(targetLanguageCode);
        translationRequest.setRequestId(requestId);
        translationRequest.setStatus((requestId > 0) ? TranslationRequestStatusEnum.SENT.status : TranslationRequestStatusEnum.ERROR.status);

        var resourceDataDto = new ResourceDataDto<TranslationRequest>();
        resourceDataDto.setData(translationRequest);

        ResourceDto response = translationRequestService.saveTranslationRequest(projectId, authentication, resourceDataDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projects/{projectId}/translation/{targetLanguageCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getTranslationsForTargetLanguageCode(
            Authentication authentication,
            @PathVariable String projectId,
            @PathVariable String targetLanguageCode,
            @RequestParam String applicant,
            @RequestParam(required = false) String translationFilter,
            @RequestParam(required = false) String targetTranslationFilter,
            @RequestParam(required = false) String targetStatusFilter,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "25") int size) {

        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponseWithPage();

        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId, authentication, targetLanguageCode);

        if(languageData == null){
            throw new TranslationFailed("Unrecognized language: " + targetLanguageCode);
        }

        Page<ResourceDto> result = languageTranslationsService.getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCode(projectId, authentication, targetLanguageCode, translationFilter, targetTranslationFilter, targetStatusFilter, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/projects/{projectId}/translation/{targetLanguageCode}/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getTranslationsForTargetLanguageCodeAndKey(
            Authentication authentication,
            @PathVariable String projectId,
            @PathVariable String targetLanguageCode,
            @PathVariable String key,
            @RequestParam String applicant) {

        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponse();

        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId, authentication, targetLanguageCode);
        if(languageData == null){
            throw new TranslationFailed("Unrecognized language: " + targetLanguageCode);
        }

        ResourceDto result = languageTranslationsService.getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCodeAndKey(projectId, authentication, targetLanguageCode, key);
        return ResponseEntity.ok(result);

    }

    @GetMapping(path = "/projects/{projectId}/translation/check-translation-request/{targetLanguageCode}/{externalReference}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getTranslationRequestsFilterByStatus(
            Authentication authentication,
            @PathVariable String projectId,
            TranslationRequestFilter filter,
            @RequestParam String applicant) {

        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponse();

        var result = translationRequestService.getTranslationRequestWithFilter(projectId, authentication, filter, false);

        return ResponseEntity.ok(result);

    }

    @PostMapping(value = "/projects/{projectId}/translation/change-translations-status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDto> changeTranslationStatusRequest(Authentication authentication,
                                                               @RequestParam String applicant,
                                                               @PathVariable String projectId,
                                                               @RequestParam String status,
                                                               @RequestParam String languageCode,
                                                               @RequestParam(required = false) String key) {
        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponse();

        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId,authentication, languageCode);
        if(languageData == null){
            throw new TranslationFailed("Unrecognized language: " + languageCode);
        }

        LanguageTranslationsFilter languageTranslationFilter = new LanguageTranslationsFilter();
        languageTranslationFilter.setLanguage(languageData.getData().get(configuration.getLanguageLong()).toString());
        languageTranslationFilter.setKey(key);

        languageTranslationsService.updateAllSubmissionsStatusByFilter(projectId, authentication, status, languageTranslationFilter);
        cacheService.invalidateCache(LanguageTranslationsServiceImpl.GET_LANGUAGE_TRANSLATIONS_BY_ISO_CODE_AND_STATUS_CACHE);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/projects/{projectId}/translation",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceDto> createUpdateLanguageTranslation(Authentication authentication,
                                                                      @RequestParam String applicant,
                                                                      @PathVariable String projectId,
                                                                      @RequestParam String languageCode,
                                                                      @RequestParam String key,
                                                                      @RequestParam String translation) {
        if (!checkForAdminRole(authentication, projectId, applicant)) return getForbiddenResponse();

        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId, authentication, languageCode);
        if(languageData == null){
            throw new TranslationFailed("Unrecognized language: " + languageCode);
        }

        ResourceDto translationData = languageTranslationsService.getLanguageTranslationsByDefaultIsoCodeAndKey(projectId,authentication,key);
        if(translationData == null){
            throw new TranslationFailed("No translation in default language for key: " + key);
        }

        var languageLong = languageData.getData().get(configuration.getLanguageLong()).toString();
        var languageTranslationsFilter = new LanguageTranslationsFilter();
        languageTranslationsFilter.setLanguage(languageLong);
        languageTranslationsFilter.setKey(key);

        var languageTranslationPath = new ResourcePath(projectId, configuration.getLanguageTranslations());
        var languageTranslation = languageTranslationsService.getLanguageTranslationsWithFilter(projectId, authentication, languageTranslationsFilter);

        var createSubmissionData = new HashMap<String, Object>();
        createSubmissionData.put(configuration.getTranslation(), translation);
        createSubmissionData.put(configuration.getStatus(), LanguagesStatusEnum.HIDDEN.status);
        createSubmissionData.put(configuration.getKey(), key);
        createSubmissionData.put(configuration.getLanguage(), languageLong);
        createSubmissionData.put(configuration.getIdentifier(), languageLong + configuration.getDashSeparator() + key);

        var doNotUpdateProps = List.of(configuration.getLanguage(), configuration.getKey(), configuration.getStatus(), configuration.getIdentifier());
        ResourceDto result = null;

        try {
            result = updateResource(languageTranslationPath, authentication, languageTranslation, createSubmissionData, doNotUpdateProps);
        } catch (StoreDataException e) {
            throw new TranslationFailed("Translation update failed! Reason: " + e.getMessage());
        }

        cacheService.invalidateCache(LanguageTranslationsServiceImpl.GET_LANGUAGE_TRANSLATIONS_BY_ISO_CODE_AND_STATUS_CACHE);

        return ResponseEntity.ok(result);
    }

    private ResourceDto updateResource(ResourcePath path, Authentication authentication, ResourceDto oldSubmission,
                                       HashMap<String, Object> newSubmissionData, List<String> notUpdatedProperties) {
        try {
            if (oldSubmission != null) {
                for (String param : notUpdatedProperties) {
                    newSubmissionData.remove(param);
                }
                return submissionService.updateSubmission(path, authentication, oldSubmission.get_id(), submissionPatchService.createPatchData(newSubmissionData));
            }
            return submissionService.createSubmission(path, authentication, CommonUtils.getJsonString(newSubmissionData));
        } catch (Exception exception) {
            throw new StoreDataException(exception.getMessage());
        }
    }

    private boolean checkForAdminRole(Authentication authentication, @PathVariable String projectId, @RequestParam String applicant) {
        if (StringUtils.isEmpty(applicant)) return false;

        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.Admin)) {
            return true;
        }
        return false;
    }

    private ResponseEntity<ResourceDto> getForbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Page<ResourceDto>> getForbiddenResponseWithPage() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}
