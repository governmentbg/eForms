package com.bulpros.integrations.eTranslation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TranslationRequest {
    private String externalReference;
    private CallerInformation callerInformation;
    private String textToTranslate;
    private String sourceLanguage;
    private List<String> targetLanguages;
    private String domain;
    private String requesterCallback;
    private String errorCallback;
    private Destinations destinations;
}
