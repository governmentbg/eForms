package com.bulpros.eforms.signature.model;

import eu.europa.esig.dss.enumerations.ASiCContainerType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import lombok.Data;

@Data
public class DefaultParameters {
    private SignatureForm signatureForm;

    private SignatureLevel signatureLevel;
    
    private SignaturePackaging signaturePackaging;
    
    private ASiCContainerType containerType;
    
    private DigestAlgorithm digestAlgorithm;
}
