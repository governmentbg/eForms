package com.bulpros.integrations.borica.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

@Getter
@Setter
public class SignDocumentsRequest {

    private String relyingPartyCallbackId;

    private Content[] contents;

    @Getter
    @Setter
    public static class Content {
        private String confirmText;
        private BoricaContentFormatEnum contentFormat;
        private String mediaType;
        private String data;
        private String fileName;
        private Boolean padesVisualSignature;
        private SignaturePosition signaturePosition;
    }

    @Getter
    @Setter
    public static class SignaturePosition {
        private BigDecimal imageHeight;
        private BigDecimal imageWidth;
        private BigDecimal imageXAxis;
        private BigDecimal imageYAxis;
        private BigDecimal pageNumber;
    }
}

