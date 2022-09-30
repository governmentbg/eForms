package com.bulpros.eforms.signature.model;

import eu.europa.esig.dss.enumerations.ASiCContainerType;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;

public class CAdESDefaultParameters extends DefaultParameters {
    public CAdESDefaultParameters() {
        setSignatureForm(SignatureForm.CAdES);
        setSignatureLevel(SignatureLevel.CAdES_BASELINE_B);
        setSignaturePackaging(SignaturePackaging.DETACHED);
        setContainerType(ASiCContainerType.ASiC_S);
        setDigestAlgorithm(DigestAlgorithm.SHA256);
    }
}
