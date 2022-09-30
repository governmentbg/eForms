package com.bulpros.integrations.borica.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoricaSignDocumentsRequest {

    private String relyingPartyCallbackId;
    private String callbackURL;
    private BoricaPayerEnum payer;
    private Boolean isLogin;

    private Content[] contents;

    @Getter
    @Setter
    public static class Content {
        private String confirmText;
        private BoricaContentFormatEnum contentFormat;
        private String data;
        private String fileName;
        private BoricaHashAlgorithmEnum hashAlgorithm;
        private Boolean padesVisualSignature;
        private SignaturePosition signaturePosition;
        private BoricaSignatureTypeEnum signatureType;
        private Boolean toBeArchived;
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

    public BoricaSignDocumentsRequest(SignDocumentsRequest request) {
        relyingPartyCallbackId = request.getRelyingPartyCallbackId();
        callbackURL = null;
        payer = BoricaPayerEnum.CLIENT;
        isLogin = false;

        contents = new Content[request.getContents().length];
        for (int i = 0; i < request.getContents().length; i++) {
            contents[i] = new Content();
            contents[i].confirmText = request.getContents()[i].getConfirmText();
            contents[i].contentFormat = request.getContents()[i].getContentFormat();
            contents[i].data = request.getContents()[i].getData();
            contents[i].fileName = request.getContents()[i].getFileName();
            contents[i].hashAlgorithm = BoricaHashAlgorithmEnum.SHA256;
            contents[i].padesVisualSignature = request.getContents()[i].getPadesVisualSignature();
            if (contents[i].padesVisualSignature) {
                if (request.getContents()[i].getSignaturePosition() != null) {
                    contents[i].signaturePosition = new SignaturePosition();
                    contents[i].signaturePosition.imageHeight =
                            request.getContents()[i].getSignaturePosition().getImageHeight();
                    contents[i].signaturePosition.imageWidth =
                            request.getContents()[i].getSignaturePosition().getImageWidth();
                    contents[i].signaturePosition.imageXAxis =
                            request.getContents()[i].getSignaturePosition().getImageXAxis();
                    contents[i].signaturePosition.imageYAxis =
                            request.getContents()[i].getSignaturePosition().getImageYAxis();
                    contents[i].signaturePosition.pageNumber =
                            request.getContents()[i].getSignaturePosition().getPageNumber();
                } else {
                    contents[i].padesVisualSignature = false;
                }
            }
            // TODO May be implementation from signature service should be used
            if (MediaType.APPLICATION_PDF_VALUE.equals(request.getContents()[i].getMediaType())) {
                contents[i].signatureType = BoricaSignatureTypeEnum.PADES_BASELINE_B;
            } else if (MediaType.APPLICATION_XML_VALUE.equals(request.getContents()[i].getMediaType())) {
                contents[i].signatureType = BoricaSignatureTypeEnum.XADES_BASELINE_B_ENVELOPED;
            } else {
                contents[i].signatureType = BoricaSignatureTypeEnum.CADES_BASELINE_B_DETACHED;
            }
            contents[i].toBeArchived = false;
        }
    }
}

