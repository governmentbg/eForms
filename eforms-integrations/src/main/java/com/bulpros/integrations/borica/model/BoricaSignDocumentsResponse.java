package com.bulpros.integrations.borica.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoricaSignDocumentsResponse {

    private String responseCode;
    private String code;
    private String message;
    private SignDocumentsData data;

    @Getter
    @Setter
    public static class SignDocumentsData {
        String callbackId;
        Date validity;
    }
}

