package com.bulpros.eforms.signature.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.EncryptionAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
public class DataToSignRequest implements Serializable {
    
    private SignatureForm signatureForm;

    private SignatureLevel signatureLevel;
    
    private String signatureValue;

    private DigestAlgorithm digestAlgorithm;
    
    @NotNull
    private String signingCertificate;
    
    @NotNull
    private List<String> certificateChain;

    @NotNull
    private EncryptionAlgorithm encryptionAlgorithm;
    
    private Date signingDate;

    private boolean signWithExpiredCertificate;
    
    public void fillWith(DefaultParameters parameters) {
        setSignatureForm(parameters.getSignatureForm());
        setSignatureLevel(parameters.getSignatureLevel());
        setDigestAlgorithm(parameters.getDigestAlgorithm());
    }
}
