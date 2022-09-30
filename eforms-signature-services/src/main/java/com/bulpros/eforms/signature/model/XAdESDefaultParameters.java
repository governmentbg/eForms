package com.bulpros.eforms.signature.model;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;

public class XAdESDefaultParameters extends DefaultParameters {
    public XAdESDefaultParameters() {
        setSignatureForm(SignatureForm.XAdES);
        setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
        setSignaturePackaging(SignaturePackaging.ENVELOPED);
        setDigestAlgorithm(DigestAlgorithm.SHA256);
    }
}
