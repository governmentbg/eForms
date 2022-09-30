package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;
import lombok.Setter;
import java.beans.ConstructorProperties;
import java.io.Serializable;

@Getter
@Setter
public class TranslationResponseBody implements Serializable {
    private static final long serialVersionUID = 1L;

    public String requestId;
    public String targetLanguage;
    public String translatedText;
    public String externalReference;

    @ConstructorProperties({"request-id", "target-language", "translated-text", "external-reference"})
    public TranslationResponseBody(String requestId, String targetLanguage, String translatedText, String externalReference) {
        this.requestId = requestId;
        this.targetLanguage = targetLanguage;
        this.translatedText = translatedText;
        this.externalReference = externalReference;
    }
}
