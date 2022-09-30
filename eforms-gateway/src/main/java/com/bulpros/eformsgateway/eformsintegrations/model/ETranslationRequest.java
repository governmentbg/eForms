package com.bulpros.eformsgateway.eformsintegrations.model;

import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ETranslationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String textToTranslate;
    private String sourceLanguage;
    private List<String> targetLanguages;
    private String externalReference;
    private String errorCallback;
    private String requesterCallback;

}
