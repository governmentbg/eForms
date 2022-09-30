package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

@Getter
@Setter
@NoArgsConstructor
public class EvrotrustSignDocumentDataRequest {

    private String vendorNumber;
    private String publicKey;

    private DocumentInfo document;
    private SignInfo signInfo;
    private User[] users;

    @Getter
    @Setter
    public static class DocumentInfo {
        private String description;
        private Long dateExpire;
        private Byte certificateType;
        private Long coverage;
        private Byte preview;
    }

    @Getter
    @Setter
    public static class SignInfo {
        private String type;
        private String algorithm;
    }

    @Getter
    @Setter
    public static class User {
        private String identificationNumber;
    }

    public EvrotrustSignDocumentDataRequest(String publicKey, String vendorNumber,
                                            SignDocumentsRequest signDocumentsRequest) {
        this.vendorNumber = vendorNumber;
        this.publicKey = publicKey;

        this.document = new DocumentInfo();
        this.document.description = "Please sign document: " + signDocumentsRequest.getDocuments()[0].getFileName();
        this.document.dateExpire = signDocumentsRequest.getDateExpire().getTime() / 1000;
        this.document.certificateType = 1;
        this.document.coverage = 20000L;

        this.signInfo = new SignInfo();
        this.signInfo.algorithm = "SHA256";
        if (MediaType.APPLICATION_PDF_VALUE.equals(signDocumentsRequest.getDocuments()[0].getContentType())) {
            this.signInfo.type = "PDF1";
            this.document.preview = 1;
        } else if (MediaType.APPLICATION_XML_VALUE.equals(signDocumentsRequest.getDocuments()[0].getContentType())) {
            this.signInfo.type = "XML3";
            this.document.preview = 0;
        } else {
            this.signInfo.type = "CAD1";
            this.document.preview = 0;
        }

        this.users = new User[signDocumentsRequest.getUserIdentifiers().length];
        for (int i = 0; i < signDocumentsRequest.getUserIdentifiers().length; i++) {
            users[i] = new User();
            users[i].setIdentificationNumber(signDocumentsRequest.getUserIdentifiers()[i]);
        }
    }
}
