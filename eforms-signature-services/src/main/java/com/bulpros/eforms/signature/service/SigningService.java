package com.bulpros.eforms.signature.service;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bulpros.eforms.signature.exception.SignatureRequestException;
import com.bulpros.eforms.signature.model.CAdESDefaultParameters;
import com.bulpros.eforms.signature.model.DataToSignRequest;
import com.bulpros.eforms.signature.model.DefaultParameters;
import com.bulpros.eforms.signature.model.DigestToSignRequest;
import com.bulpros.eforms.signature.model.DocumentToSignRequest;
import com.bulpros.eforms.signature.model.PAdESDefaultParameters;
import com.bulpros.eforms.signature.model.XAdESDefaultParameters;
import com.bulpros.eforms.signature.utils.DocumentUtils;
import com.google.common.net.MediaType;

import eu.europa.esig.dss.AbstractSignatureParameters;
import eu.europa.esig.dss.asic.cades.ASiCWithCAdESSignatureParameters;
import eu.europa.esig.dss.asic.cades.signature.ASiCWithCAdESService;
import eu.europa.esig.dss.cades.CAdESSignatureParameters;
import eu.europa.esig.dss.cades.signature.CAdESService;
import eu.europa.esig.dss.enumerations.ASiCContainerType;
import eu.europa.esig.dss.enumerations.SignatureAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.DigestDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SigningService {
    @Autowired
    private CAdESService cadesService;
    
    @Autowired
    private ASiCWithCAdESService asicWithCAdESService;

    @Autowired
    private PAdESService padesService;
    
    @Autowired
    private XAdESService xadesService;
    
    private UrlValidator urlValidator = UrlValidator.getInstance();
    
    private Tika tika = new Tika();
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ToBeSigned getDataToSign(DocumentToSignRequest request) {
        log.info(String.format("Start getDataToSign with one document: %s", request.getDocumentName()));
        DocumentSignatureService service = getSignatureService(request.getContainerType(), request.getSignatureForm());

        AbstractSignatureParameters parameters = fillParameters(request);

        try {
            String documentToSign = request.getDocumentToSign();
            DSSDocument toSignDocument = DocumentUtils.toDSSDocument(documentToSign, request.getDocumentName());
            ToBeSigned toBeSigned = service.getDataToSign(toSignDocument, parameters);
            log.info("End getDataToSign with one document");
            return toBeSigned;
        } catch (Exception e) {
            throw new SignatureRequestException(e.getMessage());
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ToBeSigned getDataToSign(DigestToSignRequest request) {
        log.info(String.format("Start getDataToSign with one digest: %s", request.getDigestToSign()));
        DocumentSignatureService service = getSignatureService(null, request.getSignatureForm());

        AbstractSignatureParameters parameters = fillParameters(request);

        try {
            DigestDocument toSignDigest = new DigestDocument(request.getDigestAlgorithm(), request.getDigestToSign(), request.getDocumentName());
            ToBeSigned toBeSigned = service.getDataToSign(toSignDigest, parameters);
            log.info("End getDataToSign with one digest");
            return toBeSigned;
        } catch (Exception e) {
            throw new SignatureRequestException(e.getMessage());
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DSSDocument signDigest(DigestToSignRequest request) {
        log.info(String.format("Start signDigest with one digest: %s", request.getDigestToSign()));
        DocumentSignatureService service = getSignatureService(null, request.getSignatureForm());

        AbstractSignatureParameters parameters = fillParameters(request);

        try {
            DigestDocument toSignDigest = new DigestDocument(request.getDigestAlgorithm(), request.getDigestToSign(), request.getDocumentName());
            SignatureAlgorithm sigAlgorithm = SignatureAlgorithm.getAlgorithm(request.getEncryptionAlgorithm(), request.getDigestAlgorithm());
            SignatureValue signatureValue = new SignatureValue(sigAlgorithm, Utils.fromBase64(request.getSignatureValue()));
            DSSDocument signedDocument = service.signDocument(toSignDigest, parameters, signatureValue);
            log.info("End signDigest with one digest");
            return signedDocument;
        } catch (Exception e) {
            log.error(String.format("Problem in signDigest with one digest: %s", e.getMessage()));
            throw new SignatureRequestException(e.getMessage());
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DSSDocument signDocument(DocumentToSignRequest request) {
        log.info("Start signDocument with one document: %s", request.getDocumentName());
        DocumentSignatureService service = getSignatureService(request.getContainerType(), request.getSignatureForm());

        AbstractSignatureParameters parameters = fillParameters(request);

        try {
            DSSDocument toSignDocument = DocumentUtils.toDSSDocument(request.getDocumentToSign(), request.getDocumentName());
            SignatureAlgorithm sigAlgorithm = SignatureAlgorithm.getAlgorithm(request.getEncryptionAlgorithm(), request.getDigestAlgorithm());
            SignatureValue signatureValue = new SignatureValue(sigAlgorithm, Utils.fromBase64(request.getSignatureValue()));
            DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValue);
            log.info("End signDocument with one document");
            return signedDocument;
        } catch (Exception e) {
            log.error(String.format("Problem in signDocument with one document: %s", e.getMessage()));
            throw new SignatureRequestException(e.getMessage());
        }
    }
    /**
     * Tries to get the appropriate signature form
     * depending on the document type or return CAdES
     * 
     * @param request
     * @return
     */
    public DocumentToSignRequest fillSignatureParametersByDocument(DocumentToSignRequest request) {
        DefaultParameters parameters = new CAdESDefaultParameters();
        MediaType mediaType = null;
        String document = request.getDocumentToSign();
        if (Utils.isBase64Encoded(document)) {
            mediaType = MediaType.parse(tika.detect(Utils.fromBase64(document)));
        } else if (urlValidator.isValid(document)) {
            try {
                mediaType = MediaType.parse(tika.detect(new URL(document)));
            } catch (IOException e) {
                log.warn(String.format("Couldn't get mime type: %s", e.getMessage()));
            }
        }
        if (mediaType != null) {
            if (mediaType.is(MediaType.PDF)) {
                parameters = new PAdESDefaultParameters();
            } else if (mediaType.subtype().contains("xml")) {
                parameters = new XAdESDefaultParameters();
            }
        }
        request.fillWith(parameters);
        
        return request;
    }
    
    @SuppressWarnings({ "rawtypes" })
    protected DocumentSignatureService getSignatureService(ASiCContainerType containerType, SignatureForm signatureForm) {
        DocumentSignatureService service = null;
        if (containerType != null) {
            service = asicWithCAdESService;
        } else {
            switch (signatureForm) {
            case CAdES:
                service = cadesService;
                break;
            case PAdES:
                service = padesService;
                break;
            case XAdES:
                service = xadesService;
                break;
            default:
                log.error(String.format("Unknow signature form: %s", signatureForm));
            }
        }
        return service;
    }
    
    @SuppressWarnings("rawtypes")
    protected AbstractSignatureParameters fillParameters(DocumentToSignRequest request) {
        AbstractSignatureParameters parameters = getSignatureParameters(request.getContainerType(), request.getSignatureForm());
        parameters.setSignaturePackaging(request.getSignaturePackaging());

        fillParameters(parameters, request);

        return parameters;
    }
    
    @SuppressWarnings("rawtypes")
    protected AbstractSignatureParameters fillParameters(DigestToSignRequest request) {
        AbstractSignatureParameters parameters = getSignatureParameters(null, request.getSignatureForm());
        parameters.setSignaturePackaging(SignaturePackaging.DETACHED);

        fillParameters(parameters, request);

        return parameters;
    }
    
    @SuppressWarnings("rawtypes")
    protected void fillParameters(AbstractSignatureParameters parameters, DataToSignRequest request) {
        parameters.setSignatureLevel(request.getSignatureLevel());
        parameters.setDigestAlgorithm(request.getDigestAlgorithm());
        // parameters.setEncryptionAlgorithm(form.getEncryptionAlgorithm()); retrieved from certificate
        parameters.bLevel().setSigningDate(request.getSigningDate());

        parameters.setSignWithExpiredCertificate(request.isSignWithExpiredCertificate());

        CertificateToken signingCertificate = DSSUtils.loadCertificateFromBase64EncodedString(request.getSigningCertificate());
        parameters.setSigningCertificate(signingCertificate);

        List<String> base64CertificateChain = request.getCertificateChain();
        if (Utils.isCollectionNotEmpty(base64CertificateChain)) {
            List<CertificateToken> certificateChain = new LinkedList<>();
            for (String base64Certificate : base64CertificateChain) {
                certificateChain.add(DSSUtils.loadCertificateFromBase64EncodedString(base64Certificate));
            }
            parameters.setCertificateChain(certificateChain);
        }
    }
    
    @SuppressWarnings("rawtypes")
    protected AbstractSignatureParameters getSignatureParameters(ASiCContainerType containerType, SignatureForm signatureForm) {
        AbstractSignatureParameters parameters = null;
        if (containerType != null) {
            parameters = getASiCSignatureParameters(containerType, signatureForm);
        } else {
            switch (signatureForm) {
            case CAdES:
                parameters = new CAdESSignatureParameters();
                break;
            case PAdES:
                PAdESSignatureParameters padesParams = new PAdESSignatureParameters();
                padesParams.setContentSize(9472 * 2); // double reserved space for signature
                parameters = padesParams;
                break;
            case XAdES:
                parameters = new XAdESSignatureParameters();
                break;
            default:
                log.error(String.format("Unknown signature form: %s", signatureForm));
            }
        }
        return parameters;
    }
    
    @SuppressWarnings("rawtypes")
    protected AbstractSignatureParameters getASiCSignatureParameters(ASiCContainerType containerType, SignatureForm signatureForm) {
        AbstractSignatureParameters parameters = null;
        switch (signatureForm) {
        case CAdES:
            ASiCWithCAdESSignatureParameters asicCadesParams = new ASiCWithCAdESSignatureParameters();
            asicCadesParams.aSiC().setContainerType(containerType);
            parameters = asicCadesParams;
            break;
        default:
            log.error(String.format("Unknow signature form %s for ASiC container.", signatureForm));
        }
        return parameters;
    }
}
