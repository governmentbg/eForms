package com.bulpros.eformsgateway.form.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TranslationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String externalReference;
    private String targetLanguageCode;
    private int requestId;
    private String translatedText;
    private String status;
    private String externalTranslationServiceErrorMessage;
    private String externalTranslationServiceErrorCode;
}
