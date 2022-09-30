package com.bulpros.eformsgateway.form.service;

import com.bulpros.eformsgateway.form.model.TranslationRequest;
import com.bulpros.eformsgateway.form.web.controller.dto.ResourceDataDto;
import com.bulpros.eformsgateway.form.web.controller.dto.TranslationRequestFilter;
import com.bulpros.formio.dto.ResourceDto;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;


public interface TranslationRequestService {

    ResourceDto saveTranslationRequest(String projectId, Authentication authentication, ResourceDataDto<TranslationRequest> translationRequest);
    ResourceDto getTranslationRequestWithFilter(String projectId, Authentication authentication, TranslationRequestFilter translationRequestFilter, boolean filterByRequestId);
}
