package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.form.exception.InvalidLanguageStatusChangeException;
import com.bulpros.eformsgateway.form.utils.CommonUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.LanguageTranslationsFilter;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import com.bulpros.formio.utils.Page;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import static com.bulpros.eformsgateway.cache.service.CacheService.PUBLIC_CACHE;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bulpros.eformsgateway.cache.service.CacheService.*;

@Service
@Slf4j
public class LanguageTranslationsServiceImpl implements LanguageTranslationsService {

    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;
    private final LanguagesService languagesService;
    private final SubmissionPatchService submissionPatchService;
    // Do not remove: Used in @Cacheable condition SpEL expression
    @Getter private final CacheService cacheService;
    public static final String GET_LANGUAGE_TRANSLATIONS_BY_ISO_CODE_AND_STATUS_CACHE = "getLanguageTranslationsByIsoCodeAndStatusCache";

    public LanguageTranslationsServiceImpl(SubmissionService submissionService, ConfigurationProperties configuration, LanguagesService languagesService, SubmissionPatchService submissionPatchService, CacheService cacheService) {
        this.submissionService = submissionService;
        this.configuration = configuration;
        this.languagesService = languagesService;
        this.submissionPatchService = submissionPatchService;
        this.cacheService = cacheService;
    }

    @Override
    @Cacheable(value = GET_LANGUAGE_TRANSLATIONS_BY_ISO_CODE_AND_STATUS_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#isoCode, #status)", unless = "#result == null", condition = CACHE_ACTIVE_CONDITION)
    public JSONObject getLanguageTranslationsByIsoCodeAndStatus(String projectId, String isoCode, String status) {
        return getLanguageTranslationsByIsoCodeAndStatus(projectId, isoCode, status, PUBLIC_CACHE);
    }

    @Override
    @Cacheable(value = GET_LANGUAGE_TRANSLATIONS_BY_ISO_CODE_AND_STATUS_CACHE, key = "new org.springframework.cache.interceptor.SimpleKey(#isoCode, #status)", unless = "#result == null", condition = CACHE_CONTROL_CONDITION)
    public JSONObject getLanguageTranslationsByIsoCodeAndStatus(String projectId, String isoCode, String status, String cacheControl) {

        String languageFromIsoCode = null;
        JSONObject jsonObject = new JSONObject();

        List<ResourceDto> languagesByStatus = languagesService.getLanguagesByStatus(projectId, status);

        for(ResourceDto resourceDto : languagesByStatus){
            if(isoCode.equals(resourceDto.getData().get(configuration.getLanguage()).toString())){
                languageFromIsoCode = resourceDto.getData().get(configuration.getLanguageLong()).toString();
            }
        }

        if(languageFromIsoCode != null){
            var languageTranslationsByLanguageFilter = new ArrayList<SubmissionFilter>();

            languageTranslationsByLanguageFilter.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                    Map.of(configuration.getLanguage(), List.of(languageFromIsoCode),
                            configuration.getStatus(), LanguagesStatusEnum.PUBLIC.status)));

            languageTranslationsByLanguageFilter.add(SubmissionFilter.build("select", "data." + configuration.getKey()));
            languageTranslationsByLanguageFilter.add(SubmissionFilter.build("select", "data." + configuration.getTranslation()));

            Authentication authentication = AuthenticationService.createServiceAuthentication();
            ResourcePath path = new ResourcePath(projectId, configuration.getLanguageTranslations());

            List<ResourceDto> allSubmissions = submissionService.getAllSubmissionsWithFilter(path, authentication, languageTranslationsByLanguageFilter, 100l, false);


            allSubmissions.forEach(resourceDto -> {
                jsonObject.put(resourceDto.getData().get(configuration.getKey()).toString(), resourceDto.getData().get(configuration.getTranslation()));
            });
        }

        return jsonObject;
    }

    @Override
    public ResourceDto getLanguageTranslationsByDefaultIsoCodeAndKey(String projectId, Authentication authentication, String key) {
        String defaultIsoCode = configuration.getDefaultLanguageCode();
        ResourceDto language = languagesService.getLanguageByLanguage(projectId, authentication, defaultIsoCode);

        return getLanguageTranslationByLanguageAndKey(projectId, authentication, language.getData().get(configuration.getLanguageLong()).toString(), key);

    }

    @Override
    public Page<ResourceDto> getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCode(String projectId, Authentication authentication,
                                                                                          String targetLanguageCode,
                                                                                          String defaultTextFilter,
                                                                                          String targetTextFilter,
                                                                                          String targetStatusFilter,
                                                                                          int page, int size) {
        defaultTextFilter = (!StringUtils.isEmpty(defaultTextFilter))? defaultTextFilter.strip() : "";
        targetTextFilter = (!StringUtils.isEmpty(targetTextFilter))? targetTextFilter.strip() : "";
        targetStatusFilter = (!StringUtils.isEmpty(targetStatusFilter))? targetStatusFilter.strip() : "";

        String defaultIsoCode = configuration.getDefaultLanguageCode();
        String defaultLanguageLong = getLanguageLongByLanguageCode(projectId, authentication, defaultIsoCode);
        String targetLanguageLong = getLanguageLongByLanguageCode(projectId, authentication, targetLanguageCode);

        LanguageTranslationsFilter defaultFilter = new LanguageTranslationsFilter();
        defaultFilter.setLanguage(defaultLanguageLong);
        defaultFilter.setTranslation(defaultTextFilter);
        var defaultLanguageTranslationsSubmissionsResults = getAllLanguageTranslationsWithFilter(projectId, authentication, defaultFilter, 1000L);

        LanguageTranslationsFilter targetFilter = new LanguageTranslationsFilter();
        targetFilter.setLanguage(targetLanguageLong);
        var targetLanguageTranslationsSubmissionsResult = getAllLanguageTranslationsWithFilter(projectId, authentication, targetFilter, 1000L);

        HashMap<String, ResourceDto> targetLangResultMap = new HashMap<>();
        for (ResourceDto tlRes : targetLanguageTranslationsSubmissionsResult) targetLangResultMap.put(tlRes.getData().get(configuration.getKey()).toString(),tlRes);

        List<ResourceDto> resultsList = new ArrayList<>();

        for(ResourceDto defaultLanguageTranslation : defaultLanguageTranslationsSubmissionsResults) {
            // create a combined translation entry and add fields to it
            var combinedLanguageTranslation = SerializationUtils.clone(defaultLanguageTranslation);
            var targetLanguageTranslation = targetLangResultMap.get(defaultLanguageTranslation.getData().get(configuration.getKey()));

            if(targetLanguageTranslation != null) {
                combinedLanguageTranslation.getData().put(configuration.getHasTranslation(), true);
                combinedLanguageTranslation.getData().put(configuration.getTargetTranslation(), targetLanguageTranslation.getData());
            } else {
                combinedLanguageTranslation.getData().put(configuration.getHasTranslation(), false);
                combinedLanguageTranslation.getData().put(configuration.getTargetTranslation(), null);
            }

            // apply filtering
            if(isTextFilterPassing(combinedLanguageTranslation, targetTextFilter) && isStatusFilterPassing(combinedLanguageTranslation, targetStatusFilter)) {
                resultsList.add(combinedLanguageTranslation);
            }
        }

        int originalListSize = resultsList.size();
        int fromIndex = Math.min(originalListSize, (page-1) * size);
        int toIndex = Math.min(originalListSize, page * size);

        if(page <= 0 || size <= 0) {
            return new Page<>(0l, 0l, Arrays.asList());
        }

        int totalPages = (int) Math.ceil((double) originalListSize / size);

        return new Page<>(Long.valueOf(totalPages), Long.valueOf(originalListSize), resultsList.subList(fromIndex,toIndex));
    }

    private boolean isTextFilterPassing(ResourceDto combinedLangTranslation, String textFilter) {
        if(StringUtils.isEmpty(textFilter)) {
            return true;
        } else if((boolean) combinedLangTranslation.getData().get(configuration.getHasTranslation()) == false) {
            return false;
        }
        HashMap<String, String> targetTranslationTextMap = (HashMap<String, String>) combinedLangTranslation.getData().get(configuration.getTargetTranslation());
        String translationText = targetTranslationTextMap.get(configuration.getTranslation());

        return translationText.toLowerCase().matches("(.*)" + textFilter.strip().toLowerCase() + "(.*)");
    }

    private boolean isStatusFilterPassing(ResourceDto combinedLangTranslation, String statusFilter) {

        if(StringUtils.isEmpty(statusFilter)) {
            return true;
        } else if((boolean) combinedLangTranslation.getData().get(configuration.getHasTranslation()) == false) {
            // no translation
            return (statusFilter.equals(LanguagesStatusEnum.ALL.status) || statusFilter.equals(LanguagesStatusEnum.UNTRANSLATED.status));
        } else {
            // have a filter and a translation
            HashMap<String, String> targetTranslationTextMap = (HashMap<String, String>) combinedLangTranslation.getData().get(configuration.getTargetTranslation());
            String translationStatus = targetTranslationTextMap.get(configuration.getStatus());
            return (statusFilter.equals(LanguagesStatusEnum.ALL.status) || statusFilter.equals(translationStatus));
        }
    }

    @Override
    public List<ResourceDto> getAllLanguageTranslationsWithFilter(String projectId, Authentication authentication, LanguageTranslationsFilter languageTranslationsFilter, Long bulkSize) {
        ArrayList<SubmissionFilter> filter = getLanguageTranslationsFilters(languageTranslationsFilter);
        ResourcePath path = new ResourcePath(projectId, configuration.getLanguageTranslations());
        return submissionService.getAllSubmissionsWithFilter(path, authentication, filter, bulkSize);
    }


    @Override
    public ResourceDto getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCodeAndKey(String projectId, Authentication authentication,
                                                                                          String targetLanguageCode, String key) {

        String defaultIsoCode = configuration.getDefaultLanguageCode();
        String defaultLanguageLong = getLanguageLongByLanguageCode(projectId, authentication, defaultIsoCode);
        String targetLanguageLong = getLanguageLongByLanguageCode(projectId, authentication, targetLanguageCode);

        var defaultLanguageSubmissionsResult = getLanguageTranslationByLanguageAndKey(projectId, authentication, defaultLanguageLong, key);

        if(defaultLanguageSubmissionsResult != null) {
            addTranslationProps(projectId, authentication, targetLanguageLong, key, defaultLanguageSubmissionsResult);
        }

        return defaultLanguageSubmissionsResult;
    }

    @Override
    public ResourceDto getLanguageTranslationsWithFilter(String projectId, Authentication authentication, LanguageTranslationsFilter languageTranslationsFilter) {
        ArrayList<SubmissionFilter> filter = getLanguageTranslationsFilters(languageTranslationsFilter);
        ResourcePath path = new ResourcePath(projectId, configuration.getLanguageTranslations());
        var response = submissionService.getSubmissionsWithFilter(path, authentication, filter);

        if(response.isEmpty()) return null;
        return response.get(0);
    }

    @Override
    @SneakyThrows
    public void updateAllSubmissionsStatusByFilter(String projectId, Authentication authentication, String status, LanguageTranslationsFilter languageTranslationsFilter) {

        String defaultIsoCode = configuration.getDefaultLanguageCode();
        String defaultLanguageLong = getLanguageLongByLanguageCode(projectId, authentication, defaultIsoCode);

        if(!LanguagesStatusEnum.isLanguagesStatusValid(status)) {
            throw new InvalidLanguageStatusChangeException("Invalid status specified: " + status);
        }
        if(status.equalsIgnoreCase(LanguagesStatusEnum.HIDDEN.status) && defaultLanguageLong.equals(languageTranslationsFilter.getLanguage())) {
            throw new InvalidLanguageStatusChangeException("Invalid status for default language! Status: " + status);
        }

        ArrayList<SubmissionFilter> filter = getLanguageTranslationsFilters(languageTranslationsFilter);
        ResourcePath path = new ResourcePath(projectId, configuration.getLanguageTranslations());
        var filteredLanguageTranslations = submissionService.getAllSubmissionsWithFilter(path, authentication, filter, 100L);

        HashMap<String, Object> patchEntity = new HashMap<>();
        patchEntity.put(configuration.getStatus(), status);
        String patchData = submissionPatchService.createPatchData(patchEntity);

        for(ResourceDto languageTranslation : filteredLanguageTranslations) {
            if(!languageTranslation.getData().get(configuration.getStatus()).toString().equals(status)){
                submissionService.updateSubmission(path, authentication, languageTranslation.get_id(), patchData);
            }
        }
    }

    private String getLanguageLongByLanguageCode(String projectId, Authentication authentication, String languageCode) {
        ResourceDto languageData = languagesService.getLanguageByLanguage(projectId, authentication, languageCode);
        return (languageData != null) ? (String) languageData.getData().get(configuration.getLanguageLong()) : "";
    }

    private ResourceDto addTranslationProps(String projectId, Authentication authentication, String language, String key, ResourceDto resourceDto) {

        var targetLanguageTranslation = getLanguageTranslationByLanguageAndKey(projectId, authentication, language, key);

        if(targetLanguageTranslation != null) {
            resourceDto.getData().put(configuration.getHasTranslation(), true);
            resourceDto.getData().put(configuration.getTargetTranslation(), targetLanguageTranslation.getData());
        } else {
            resourceDto.getData().put(configuration.getHasTranslation(), false);
            resourceDto.getData().put(configuration.getTargetTranslation(), null);
        }

        return resourceDto;
    }

    private ResourceDto getLanguageTranslationByLanguageAndKey(String projectId, Authentication authentication, String language, String key) {

        var languageFilter = new ArrayList<SubmissionFilter>();
        languageFilter.add(new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                Map.of(configuration.getLanguage(), List.of(language), configuration.getKey(), List.of(key))));

        ResourcePath path = new ResourcePath(projectId, configuration.getLanguageTranslations());
        var response = submissionService.getSubmissionsWithFilter(path, authentication, languageFilter);

        if(response.isEmpty()) return null;
        return response.get(0);
    }

    @NotNull
    private ArrayList<SubmissionFilter> getLanguageTranslationsFilters(LanguageTranslationsFilter languageTranslationsFilter) {
        var filter = new ArrayList<SubmissionFilter>();

        if (!StringUtils.isEmpty(languageTranslationsFilter.getLanguage())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getLanguage(), languageTranslationsFilter.getLanguage())));
        }
        if (!StringUtils.isEmpty(languageTranslationsFilter.getKey())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getKey(), languageTranslationsFilter.getKey())));
        }
        if (!StringUtils.isEmpty(languageTranslationsFilter.getTranslation())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.REGEX,
                    Collections.singletonMap(configuration.getTranslation(), "(.*)" + languageTranslationsFilter.getTranslation().strip() + "(.*)")));
        }
        if (!StringUtils.isEmpty(languageTranslationsFilter.getStatus())) {
            filter.add(new SubmissionFilter(
                    SubmissionFilterClauseEnum.NONE,
                    Collections.singletonMap(configuration.getStatus(), languageTranslationsFilter.getStatus())));
        }
        return filter;
    }

}
