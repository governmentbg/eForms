package com.bulpros.eforms.signature.controller;

import java.util.Date;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bulpros.eforms.signature.model.CAdESDefaultParameters;
import com.bulpros.eforms.signature.model.DataToSignResponse;
import com.bulpros.eforms.signature.model.DefaultParameters;
import com.bulpros.eforms.signature.model.DigestToSignRequest;
import com.bulpros.eforms.signature.model.SignedDocumentResponse;
import com.bulpros.eforms.signature.service.SigningService;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.DSSUtils;

@Controller
@RequestMapping(value = "/api/signature/digest")
public class DigestSignatureController {
    
    @Autowired
    private SigningService signingService;
    
    @RequestMapping(value = "/data", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DataToSignResponse getDataToSign(@RequestBody @Valid DigestToSignRequest request) {
        
        request = fillDefaultSignatureParameters(request);
        
        ToBeSigned dataToSign = signingService.getDataToSign(request);
        if (dataToSign == null) {
            return null;
        }

        DataToSignResponse response = new DataToSignResponse();
        response.setDataToSign(DatatypeConverter.printBase64Binary(dataToSign.getBytes()));
        return response;
    }
    
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ResponseBody
    public SignedDocumentResponse sign(@RequestBody @Valid DigestToSignRequest request) {
        
        request = fillDefaultSignatureParameters(request);

        DSSDocument document = signingService.signDigest(request);

        SignedDocumentResponse signedDocumentResponse = new SignedDocumentResponse(
                document.getName(), 
                document.getMimeType(),
                DSSUtils.toByteArray(document), 
                request.getDigestAlgorithm());
        
        return signedDocumentResponse;
    }
    
    private DigestToSignRequest fillDefaultSignatureParameters(DigestToSignRequest request) {
        DefaultParameters parameters = new CAdESDefaultParameters();
        request.fillWith(parameters);
        if (request.getSigningDate() == null) {
            request.setSigningDate(new Date());
        }
        return request;
    }
}
