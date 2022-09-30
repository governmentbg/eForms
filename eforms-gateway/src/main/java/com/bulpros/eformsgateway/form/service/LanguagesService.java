package com.bulpros.eformsgateway.form.service;

import com.bulpros.formio.dto.ResourceDto;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface LanguagesService {

    List<ResourceDto> getLanguagesByStatus(String projectId, String status);
    ResourceDto getLanguageByLanguage(String projectId, Authentication authentication, String language);

}
