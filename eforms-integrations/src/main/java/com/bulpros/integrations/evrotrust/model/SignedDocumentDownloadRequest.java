package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignedDocumentDownloadRequest {

    private String vendorNumber;
    private String transactionID;

    public SignedDocumentDownloadRequest(String vendorNumber, String transactionID) {
        this.vendorNumber = vendorNumber;
        this.transactionID = transactionID;
    }
}
