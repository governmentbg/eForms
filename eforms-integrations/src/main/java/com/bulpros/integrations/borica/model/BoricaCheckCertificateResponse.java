package com.bulpros.integrations.borica.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoricaCheckCertificateResponse {

    private String responseCode;
    private String code;
    private String message;
    private CertificateData data;

    @Getter
    @Setter
    public static class CertificateData {
        String certReqId;
        String[] devices;
        String encodedCert;
    }
}

