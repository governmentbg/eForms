package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;

@Getter
@Setter
@NoArgsConstructor
public class EvrotrustSignDocumentGroupDataRequest {

    private String vendorNumber;
    private String publicKey;

    private DocumentInfo[] documents;
    private SignInfo signInfo;
    private String groupDescription;
    private Byte certificateType;
    private Long coverage;
    private Long dateExpire;
    private User[] users;

    @Getter
    @Setter
    public static class DocumentInfo {
        private String description;
        private Integer optional;
        private Integer preview;
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

    public EvrotrustSignDocumentGroupDataRequest(String publicKey, String vendorNumber,
                                                 SignDocumentsRequest signDocumentRequest) {
        this.vendorNumber = vendorNumber;
        this.publicKey = publicKey;

        this.documents = new DocumentInfo[signDocumentRequest.getDocuments().length];
        for (int i = 0; i < signDocumentRequest.getDocuments().length; i++) {
            this.documents[i] = new DocumentInfo();
            this.documents[i].setDescription(signDocumentRequest.getDocuments()[i].getFileName());
            this.documents[i].setOptional(0);
            this.documents[i].setPreview(1);
        }

        this.signInfo = new SignInfo();
        this.signInfo.algorithm = "SHA256";
        this.signInfo.type = "PDF1";

        this.groupDescription = "Please sign documents";
        this.certificateType = 1;
        this.coverage = 20000L;
        this.dateExpire = signDocumentRequest.getDateExpire().getTime() / 1000;

        this.users = new User[signDocumentRequest.getUserIdentifiers().length];
        for (int i = 0; i < signDocumentRequest.getUserIdentifiers().length; i++) {
            users[i] = new User();
            users[i].setIdentificationNumber(signDocumentRequest.getUserIdentifiers()[i]);
        }
    }
}
