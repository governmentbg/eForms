package com.bulpros.eforms.signature.model;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.model.MimeType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignedDocumentResponse {
    /** The document name */
    private String name = "SignedDocument";
    
    private MimeType mimeType;
    
    /** Binaries of the document or its digest value (for DigestDocument) */
    private byte[] bytes;

    /** The used DigestAlgorithm in case of a DigestDocument (allows to send only the digest of the document) */
    private DigestAlgorithm digestAlgorithm;
}
