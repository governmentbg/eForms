package com.bulpros.eformsgateway.form.web.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LanguageTranslationsFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String language;
    private String key;
    private String translation;
    private String status;
}
