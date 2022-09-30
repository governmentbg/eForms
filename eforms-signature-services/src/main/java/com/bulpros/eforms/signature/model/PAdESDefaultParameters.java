package com.bulpros.eforms.signature.model;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;

public class PAdESDefaultParameters extends DefaultParameters {
    public PAdESDefaultParameters() {
        setSignatureForm(SignatureForm.PAdES);
        setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
        setSignaturePackaging(SignaturePackaging.ENVELOPED);
        setDigestAlgorithm(DigestAlgorithm.SHA256);
    }
}
