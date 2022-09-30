package com.bulpros.integrations.evrotrust.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignedDocumentDownloadResponse {

    byte[] content;
    String fileName;
    String contentType;

}
