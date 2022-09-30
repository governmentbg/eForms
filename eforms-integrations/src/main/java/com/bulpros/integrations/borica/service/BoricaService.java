package com.bulpros.integrations.borica.service;

import com.bulpros.integrations.borica.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("BoricaService")
@Slf4j
public class BoricaService {

    @Autowired
    private RestTemplate restTemplateBorica;

    @Value("${com.bulpros.borica.url}")
    private String boricaUrl;
    @Value("${com.bulpros.borica.keyStore.relyingPartyID}")
    private String boricaRelyingPartyID;

    static String checkCertificateUrl = "/v2/cert/identity/%s/%s";
    static String signDocumentsUrl = "/v2/sign";
    static String checkSignStatusUrl = "/v2/sign/%s";
    static String checkRelyingPartySignStatusUrl = "/v2/rpcallbackid/%s";
    static String getSignedDocumentUrl = "/v2/sign/content/%s";

    public BoricaCheckCertificateResponse checkCertificate(String identificatorType, String identityValue) throws Exception {
        if (BoricaIdentificatorTypeEnum.fromKey(identificatorType) == null) {
            throw new IllegalArgumentException("Invalid path parameter: " + identificatorType);
        }

        HttpEntity<String> entity = getRequestHeader();
        ResponseEntity<BoricaCheckCertificateResponse> response =
                restTemplateBorica.exchange(boricaUrl +
                                String.format(checkCertificateUrl, identificatorType, identityValue),
                        HttpMethod.GET, entity, BoricaCheckCertificateResponse.class);

        return response.getBody();
    }

    public BoricaSignDocumentsResponse signDocuments(SignDocumentsRequest signDocumentsRequest,
                                                     String rpToClientAuthorization) throws Exception {
        BoricaSignDocumentsRequest boricaRequest = new BoricaSignDocumentsRequest(signDocumentsRequest);

        HttpEntity<BoricaSignDocumentsRequest> entity = getRequestHeader(boricaRequest, rpToClientAuthorization);
        ResponseEntity<BoricaSignDocumentsResponse> response =
                restTemplateBorica.postForEntity(boricaUrl + signDocumentsUrl,
                        entity, BoricaSignDocumentsResponse.class);

        return response.getBody();
    }

    public BoricaCheckSignStatusResponse checkSignStatus(String callbackId) throws Exception {
        HttpEntity<String> entity = getRequestHeader();
        ResponseEntity<BoricaCheckSignStatusResponse> response =
                restTemplateBorica.exchange(boricaUrl +
                                String.format(checkSignStatusUrl, callbackId),
                        HttpMethod.GET, entity, BoricaCheckSignStatusResponse.class);

        return response.getBody();
    }

    public BoricaCheckSignStatusResponse checkRelyingPartySignStatus(String rpCallbackId) throws Exception {
        HttpEntity<String> entity = getRequestHeader();
        ResponseEntity<BoricaCheckSignStatusResponse> response =
                restTemplateBorica.exchange(boricaUrl +
                                String.format(checkRelyingPartySignStatusUrl, rpCallbackId),
                        HttpMethod.GET, entity, BoricaCheckSignStatusResponse.class);

        return response.getBody();
    }

    public SignedDocumentResponse getSignedDocument(String signature) throws Exception {
        HttpEntity<String> entity = getRequestContentHeader();
        ResponseEntity<byte[]> response =
                restTemplateBorica.exchange(boricaUrl +
                                String.format(getSignedDocumentUrl, signature),
                        HttpMethod.GET, entity, byte[].class);

        Map<String, String> headers = response.getHeaders().toSingleValueMap();
        String fileName = "";
        if (headers.get("Content-Disposition") != null) {
            Matcher matcher = Pattern.compile("(attachment; filename=)(.*)").matcher(headers.get("Content-Disposition"));
            if (matcher.find()) {
                fileName = matcher.group(2);
            }
        }
        String contentType = headers.get("Content-Type");

        return new SignedDocumentResponse("GENERAL_OK", "OK", "",
                fileName, contentType, response.getBody());
    }

    private HttpEntity<String> getRequestHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("relyingPartyID", boricaRelyingPartyID);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<BoricaSignDocumentsRequest> getRequestHeader(BoricaSignDocumentsRequest requestBody, String rpToClientAuthorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("relyingPartyID", boricaRelyingPartyID);
        headers.set("rpToClientAuthorization", rpToClientAuthorization);
        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<String> getRequestContentHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set("relyingPartyID", boricaRelyingPartyID);
        return new HttpEntity<>(headers);
    }
}
