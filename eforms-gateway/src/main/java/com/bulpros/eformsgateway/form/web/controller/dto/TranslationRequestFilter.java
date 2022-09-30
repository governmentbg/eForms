package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TranslationRequestFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String externalReference;
    private String targetLanguageCode;
    private int requestId;
    private String translatedText;
    private List<String> status;
}
