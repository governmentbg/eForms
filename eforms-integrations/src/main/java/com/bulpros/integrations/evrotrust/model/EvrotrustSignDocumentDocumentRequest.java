package com.bulpros.integrations.evrotrust.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class EvrotrustSignDocumentDocumentRequest {

    private byte[] value;
    private Options options;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Options {
        private String filename;
        private String contentType;
    }

    public EvrotrustSignDocumentDocumentRequest(SignDocumentsRequest.Document document) {
        this.value = Base64.getDecoder().decode(document.getContent());
        this.options = new Options(document.getFileName(), document.getContentType());
    }
}
