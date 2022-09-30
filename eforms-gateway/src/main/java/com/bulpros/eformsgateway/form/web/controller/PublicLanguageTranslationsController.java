package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.form.service.*;
import com.bulpros.eformsgateway.form.web.controller.dto.TranslationErrorBody;
import com.bulpros.eformsgateway.form.web.controller.dto.TranslationRequestFilter;
import com.bulpros.eformsgateway.form.web.controller.dto.TranslationResponseBody;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.service.SubmissionService;
import io.micrometer.core.annotation.Timed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/public")
@Slf4j
public class PublicLanguageTranslationsController {

    @Value("${com.bulpros.formio.userprofile.project.id}")
    private String projectId;

    private final LanguageTranslationsService languageTranslationsService;
    private final TranslationRequestService translationRequestService;
    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;
    private final SubmissionPatchService submissionPatchService;

    public PublicLanguageTranslationsController(LanguageTranslationsService languageTranslationsService,
                                                TranslationRequestService translationRequestService,
                                                SubmissionService submissionService,
                                                ConfigurationProperties configuration,
                                                SubmissionPatchService submissionPatchService) {
        this.languageTranslationsService = languageTranslationsService;
        this.translationRequestService = translationRequestService;
        this.submissionService = submissionService;
        this.configuration = configuration;
        this.submissionPatchService = submissionPatchService;
    }


    @Timed(value = "eforms-gateway-get-public-translations-iso.time")
    @GetMapping(path = "/projects/{projectId}/i18n/{isoCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<JSONObject> getPublicLanguageTranslationsByIsoCode(
            @PathVariable String projectId, @PathVariable String isoCode) {

        var response = languageTranslationsService.getLanguageTranslationsByIsoCodeAndStatus(projectId, isoCode, LanguagesStatusEnum.PUBLIC.status);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/translation/receive-translation/",
                    method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void receiveTranslationCallback(TranslationResponseBody body) {

        String targetLanguage = (body.getTargetLanguage() != null)? body.getTargetLanguage() : "";
        String externalReference = (body.getExternalReference() != null)? body.getExternalReference() : "";
        String requestId = (body.getRequestId() != null)? body.getRequestId() : "";
        String translatedText = (body.getTranslatedText() != null)? body.getTranslatedText() : "";

        TranslationRequestFilter filter = new TranslationRequestFilter();
        filter.setTargetLanguageCode(targetLanguage);
        filter.setExternalReference(externalReference);

        try {
            filter.setRequestId(Integer.parseInt(requestId));
        } catch (NumberFormatException e) {
            log.warn("Received translation ERROR response. Invalid RequestId: " + requestId);
        }

        Authentication authentication = AuthenticationService.createServiceAuthentication();
        ResourceDto translationRequestEntry = translationRequestService.getTranslationRequestWithFilter(projectId,
                authentication, filter, true);

        if(translationRequestEntry != null) {
            HashMap<String, Object> patchEntity = new HashMap<>();
            patchEntity.put(configuration.getTranslatedText(), translatedText);
            patchEntity.put(configuration.getStatus(), TranslationRequestStatusEnum.RECEIVED.status);

            ResourcePath path = new ResourcePath(projectId, configuration.getTranslationRequest());
            submissionService.updateSubmission(path,authentication,translationRequestEntry.get_id(),submissionPatchService.createPatchData(patchEntity));

        } else {
            log.warn("Received translation response for missing translation request. requestId: " + requestId + " externalReference: " + externalReference + " targetLanguage: " + targetLanguage);
        }

    }

    @RequestMapping(value = "/translation/receive-error/",
                    method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void receiveTranslationErrorCallback(TranslationErrorBody errorBody) {

        String requestId = (errorBody.getRequestId() != null)? errorBody.getRequestId() : "";
        String errorCode = (errorBody.getErrorCode() != null)? errorBody.getErrorCode() : "";
        List<String> targetLanguages = (errorBody.getTargetLanguages() != null)? errorBody.getTargetLanguages() : Arrays.asList();
        String externalReference = (errorBody.getExternalReference() != null)? errorBody.getExternalReference() : "";
        String errorMessage = (errorBody.getErrorMessage() != null)? errorBody.getErrorMessage() : "";

        Authentication authentication = AuthenticationService.createServiceAuthentication();
        TranslationRequestFilter filter = new TranslationRequestFilter();
        filter.setExternalReference(externalReference);

        try {
            filter.setRequestId(Integer.parseInt(requestId));
        } catch (NumberFormatException e) {
            log.warn("Received translation ERROR response. Invalid RequestId: " + requestId);
        }

        for (String targetLanguageCode : targetLanguages) {
            filter.setTargetLanguageCode(targetLanguageCode);
            ResourceDto translationRequestEntry = translationRequestService.getTranslationRequestWithFilter(
                    projectId, authentication, filter, true);

            if(translationRequestEntry != null) {

                HashMap<String, Object> patchEntity = new HashMap<>();

                patchEntity.put(configuration.getExternalTranslationServiceErrorCode(), errorCode);
                patchEntity.put(configuration.getExternalTranslationServiceErrorMessage(), errorMessage);
                patchEntity.put(configuration.getStatus(), TranslationRequestStatusEnum.ERROR.status);

                ResourcePath path = new ResourcePath(projectId, configuration.getTranslationRequest());
                submissionService.updateSubmission(path,authentication,translationRequestEntry.get_id(),submissionPatchService.createPatchData(patchEntity));

            } else {
                log.warn("Received translation ERROR response for missing translation request. " +
                        "RequestId: " + filter.getRequestId() + " externalReference: " + filter.getExternalReference());
            }

        }

    }

}