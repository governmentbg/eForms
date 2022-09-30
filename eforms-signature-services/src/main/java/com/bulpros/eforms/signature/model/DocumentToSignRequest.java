package com.bulpros.eforms.signature.model;

import javax.validation.constraints.NotNull;

import eu.europa.esig.dss.enumerations.ASiCContainerType;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
public class DocumentToSignRequest extends DataToSignRequest {
    @NotNull
    private String documentToSign;
    
    @NotNull
    private String documentName;

    private SignaturePackaging signaturePackaging;

    private ASiCContainerType containerType;
    
    public void fillWith(DefaultParameters parameters) {
        super.fillWith(parameters);
        setSignaturePackaging(parameters.getSignaturePackaging());
        setContainerType(parameters.getContainerType());
    }
}
