package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.web.controller.dto.LanguageTranslationsFilter;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.utils.Page;
import net.minidev.json.JSONObject;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface LanguageTranslationsService {

    JSONObject getLanguageTranslationsByIsoCodeAndStatus(String projectId, String isoCode, String status);
    JSONObject getLanguageTranslationsByIsoCodeAndStatus(String projectId, String isoCode, String status, String cacheControl);
    ResourceDto getLanguageTranslationsByDefaultIsoCodeAndKey(String projectId, Authentication authentication, String key);
    Page<ResourceDto> getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCode(String projectId, Authentication authentication, String targetLanguageCode, String defaultTextFilter, String targetTranslationFilter, String targetStatusFilter, int page, int size);
    ResourceDto getLanguageTranslationsByDefaultIsoCodeAndTargetLanguageCodeAndKey(String projectId, Authentication authentication, String targetLanguageCode, String key);
    ResourceDto getLanguageTranslationsWithFilter(String projectId, Authentication authentication, LanguageTranslationsFilter languageTranslationsFilter);

    List<ResourceDto> getAllLanguageTranslationsWithFilter(String projectId, Authentication authentication, LanguageTranslationsFilter languageTranslationsFilter, Long bulkSize);
    void updateAllSubmissionsStatusByFilter(String projectId, Authentication authentication, String status, LanguageTranslationsFilter languageTranslationsFilter);
}
