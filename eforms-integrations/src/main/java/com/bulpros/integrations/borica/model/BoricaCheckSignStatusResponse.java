package com.bulpros.integrations.borica.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoricaCheckSignStatusResponse {

    private String responseCode;
    private String code;
    private String message;
    private SignStatusData data;

    @Getter
    @Setter
    public static class SignStatusData {
        Signatures[] signatures;
        String cert;
    }

    @Getter
    @Setter
    public static class Signatures {
        String signature;
        BoricaSignatureTypeEnum signatureType;
        BoricaSignStatusEnum status;
    }
}
