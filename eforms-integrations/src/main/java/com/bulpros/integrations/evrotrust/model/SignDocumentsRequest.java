package com.bulpros.integrations.evrotrust.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignDocumentsRequest {

    private Date dateExpire;
    private Document[] documents;
    private String[] userIdentifiers;

    @Getter
    @Setter
    public static class Document {
        String content;
        String fileName;
        String contentType;
    }
}
