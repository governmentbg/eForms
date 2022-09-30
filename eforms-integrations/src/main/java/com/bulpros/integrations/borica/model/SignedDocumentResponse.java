package com.bulpros.integrations.borica.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignedDocumentResponse {

    private String responseCode;
    private String code;
    private String message;
    private String fileName;
    private String contentType;
    private Object content;

}
