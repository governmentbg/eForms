package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvrotrustSignDocumentResponse {

    private String threadID;
    private Transaction[] transactions;

    @Getter
    @Setter
    public static class Transaction {
        private String transactionID;
        private String identificationNumber;
    }
}
