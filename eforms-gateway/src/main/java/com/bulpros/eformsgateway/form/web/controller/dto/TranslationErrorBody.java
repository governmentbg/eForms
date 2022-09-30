package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class TranslationErrorBody implements Serializable {
    private static final long serialVersionUID = 1L;

    public String requestId;
    public String errorCode;
    public List<String> targetLanguages;
    public String externalReference;
    public String errorMessage;

    @ConstructorProperties({"request-id", "error_code", "target-languages", "external-reference", "error-message"})
    public TranslationErrorBody(String requestId, String errorCode, List<String> targetLanguages, String externalReference, String errorMessage) {
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.targetLanguages = targetLanguages;
        this.externalReference = externalReference;
        this.errorMessage = errorMessage;
    }
}
