package com.bulpros.integrations.evrotrust.service;

import com.bulpros.integrations.evrotrust.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.RijndaelEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component("EvrotrustService")
@Slf4j
public class EvrotrustService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${com.bulpros.evrotrust.url}")
    private String evrotrustUrl;
    @Value("${com.bulpros.evrotrust.vendor.api.key}")
    private String vendorAPIKey;
    @Value("${com.bulpros.evrotrust.vendor.number}")
    private String vendorNumber;
    @Value("${com.bulpros.evrotrust.public.key}")
    private FileSystemResource publicKey;
    @Value("${com.bulpros.evrotrust.private.key}")
    private FileSystemResource privateKey;
    @Value("${com.bulpros.evrotrust.private.key.pass}")
    private String publicKeyPassphrase;
    @Value("${com.bulpros.evrotrust.group.sign.max.files}")
    private Integer groupSignMaxFiles;

    private final static String userCheckExtendedUrl = "/user/check/extended";
    private final static String signDocumentUrl = "/document/doc/online";
    private final static String checkSignDocumentStatusUrl = "/document/status";
    private final static String getSignedDocumentUrl = "/document/download";

    private final static String signDocumentGroupUrl = "/document/group/online";
    private final static String checkSignDocumentGroupStatusUrl = "/document/group/status";
    private final static String getSignedDocumentGroupUrl = "/document/group/download";

    public UserCheckExtendedResponse userCheckExtendedGet(String identifier) throws Exception {
        return userCheckExtendedPost(new UserCheckExtendedRequest(identifier, null, null, null));
    }

    public UserCheckExtendedResponse userCheckExtendedPost(UserCheckExtendedRequest userCheckExtendedRequest) throws Exception {
        EvrotrustUserCheckExtendedRequest evrotrustUserCheckExtendedRequest =
                new EvrotrustUserCheckExtendedRequest(vendorNumber, userCheckExtendedRequest);
        HttpHeaders headers = getRequestHeader(evrotrustUserCheckExtendedRequest);
        HttpEntity<EvrotrustUserCheckExtendedRequest> request =
                new HttpEntity<>(evrotrustUserCheckExtendedRequest, headers);
        ResponseEntity<UserCheckExtendedResponse> response =
                restTemplate.postForEntity(evrotrustUrl + userCheckExtendedUrl,
                        request, UserCheckExtendedResponse.class);
        return response.getBody();
    }

    public SignDocumentsResponse signDocuments(SignDocumentsRequest signDocumentsRequest) throws Exception {
        if (signDocumentsRequest.getDocuments() == null ||
                signDocumentsRequest.getDocuments().length == 0) {
            throw new Exception("At least one file must be provided !");
        } else if (signDocumentsRequest.getDocuments().length > groupSignMaxFiles) {
            throw new Exception("Up to " + groupSignMaxFiles + " files allowed !");
        } else if (signDocumentsRequest.getDocuments().length == 1) {
            return signDocument(signDocumentsRequest);
        } else {
            return signDocumentGroup(signDocumentsRequest);
        }
    }

    private SignDocumentsResponse signDocument(SignDocumentsRequest signDocumentsRequest) throws Exception {
        String key = Base64.getEncoder().encodeToString(publicKey.getInputStream().readAllBytes());
        EvrotrustSignDocumentDataRequest evrotrustDataRequest =
                new EvrotrustSignDocumentDataRequest(key, vendorNumber, signDocumentsRequest);
        EvrotrustSignDocumentDocumentRequest evrotrustDocumentRequest =
                new EvrotrustSignDocumentDocumentRequest(signDocumentsRequest.getDocuments()[0]);

        HttpEntity<MultiValueMap<String, Object>> request =
                getSignDocumentRequestHeader(evrotrustDataRequest, evrotrustDocumentRequest);
        ResponseEntity<EvrotrustSignDocumentResponse> response =
                restTemplate.postForEntity(evrotrustUrl + signDocumentUrl,
                        request, EvrotrustSignDocumentResponse.class);

        return new SignDocumentsResponse(response.getBody(), false);
    }

    private SignDocumentsResponse signDocumentGroup(SignDocumentsRequest signDocumentGroupRequest) throws Exception {
        String key = Base64.getEncoder().encodeToString(publicKey.getInputStream().readAllBytes());
        EvrotrustSignDocumentGroupDataRequest evrotrustDataRequest =
                new EvrotrustSignDocumentGroupDataRequest(key, vendorNumber, signDocumentGroupRequest);
        EvrotrustSignDocumentDocumentRequest[] evrotrustDocumentsRequest =
                new EvrotrustSignDocumentDocumentRequest[signDocumentGroupRequest.getDocuments().length];
        for (int i = 0; i < signDocumentGroupRequest.getDocuments().length; i++) {
            evrotrustDocumentsRequest[i] =
                    new EvrotrustSignDocumentDocumentRequest(signDocumentGroupRequest.getDocuments()[i]);
        }

        HttpEntity<MultiValueMap<String, Object>> request =
                getSignDocumentGroupRequestHeader(evrotrustDataRequest, evrotrustDocumentsRequest);
        ResponseEntity<EvrotrustSignDocumentResponse> response =
                restTemplate.postForEntity(evrotrustUrl + signDocumentGroupUrl,
                        request, EvrotrustSignDocumentResponse.class);

        return new SignDocumentsResponse(response.getBody(), true);
    }

    public CheckSignDocumentsStatusResponse checkSignDocumentsStatus(String transactionID, Boolean groupSigning) throws Exception {
        EvrotrustCheckSignDocumentsStatusRequest evrotrustCheckSignStatusRequest =
                new EvrotrustCheckSignDocumentsStatusRequest(vendorNumber, transactionID);
        HttpHeaders headers = getRequestHeader(evrotrustCheckSignStatusRequest);
        HttpEntity<EvrotrustCheckSignDocumentsStatusRequest> request =
                new HttpEntity<>(evrotrustCheckSignStatusRequest, headers);
        ResponseEntity<CheckSignDocumentsStatusResponse> response =
                restTemplate.postForEntity(evrotrustUrl +
                                (groupSigning ? checkSignDocumentGroupStatusUrl : checkSignDocumentStatusUrl),
                        request, CheckSignDocumentsStatusResponse.class);
        return response.getBody();
    }

    public List<SignedDocumentDownloadResponse> getSignedDocuments(String transactionID, Boolean groupSigning) throws Exception {
        if (groupSigning) {
            return getSignedDocumentGroup(transactionID);
        } else {
            SignedDocumentDownloadResponse document = getSignedDocument(transactionID);
            List<SignedDocumentDownloadResponse> response = new ArrayList<>();
            response.add(document);
            return response;
        }
    }

    private SignedDocumentDownloadResponse getSignedDocument(String transactionID) throws Exception {
        SignedDocumentDownloadRequest evrotrustDownloadRequest =
                new SignedDocumentDownloadRequest(vendorNumber, transactionID);
        HttpHeaders headers = getRequestHeader(evrotrustDownloadRequest);
        HttpEntity<SignedDocumentDownloadRequest> request =
                new HttpEntity<>(evrotrustDownloadRequest, headers);
        ResponseEntity<byte[]> response =
                restTemplate.postForEntity(evrotrustUrl + getSignedDocumentUrl,
                        request, byte[].class);

        Map<String, byte[]> inputMap = decompress(response.getBody(), false);
        byte[] decryptedData = decryptContent(inputMap.get("OUT.enc.iv"), inputMap.get("OUT.enc.key"), inputMap.get("OUT.enc"));
        Map<String, byte[]> outputMap = decompress(decryptedData, false);
        String filename = new String(inputMap.get("OUT.enc.filename"));

        return new SignedDocumentDownloadResponse(getDocument(outputMap),
                filename, getContentType(filename));
    }

    private List<SignedDocumentDownloadResponse> getSignedDocumentGroup(String transactionID) throws Exception {
        SignedDocumentDownloadRequest evrotrustDownloadRequest =
                new SignedDocumentDownloadRequest(vendorNumber, transactionID);
        HttpHeaders headers = getRequestHeader(evrotrustDownloadRequest);
        HttpEntity<SignedDocumentDownloadRequest> request =
                new HttpEntity<>(evrotrustDownloadRequest, headers);
        ResponseEntity<byte[]> response =
                restTemplate.postForEntity(evrotrustUrl + getSignedDocumentGroupUrl,
                        request, byte[].class);

        List<SignedDocumentDownloadResponse> files = new ArrayList<>();
        Map<String, byte[]> inputMap = decompress(response.getBody(), true);
        for (Map.Entry<String, byte[]> input : inputMap.entrySet()) {
            Map<String, byte[]> inputFileMap = decompress(input.getValue(), false);
            byte[] decryptedData = decryptContent(inputFileMap.get("OUT.enc.iv"), inputFileMap.get("OUT.enc.key"), inputFileMap.get("OUT.enc"));
            Map<String, byte[]> outputMap = decompress(decryptedData, false);
            String filename = new String(inputFileMap.get("OUT.enc.filename"));
            files.add(new SignedDocumentDownloadResponse(getDocument(outputMap),
                    filename, getContentType(filename)));
        }

        return files;
    }

    private Map<String, byte[]> decompress(byte[] content, boolean group) throws Exception {
        byte[] buffer = new byte[2048];
        Map<String, byte[]> map = new LinkedHashMap<>();
        try (BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(content));
             ZipInputStream stream = new ZipInputStream(bis)) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                ByteArrayOutputStream bais = new ByteArrayOutputStream();
                try (BufferedOutputStream bos = new BufferedOutputStream(bais, buffer.length)) {
                    int len;
                    while ((len = stream.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                }
                if (group) {
                    map.put(entry.getName(), bais.toByteArray());
                } else {
                    map.put(entry.getName().substring(entry.getName().indexOf("OUT")), bais.toByteArray());
                }
            }
        }
        return map;
    }

    public byte[] decryptContent(byte[] iv, byte[] key, byte[] encryptedData) throws Exception {
        try {
            Cipher keyCipher = Cipher.getInstance("RSA");
            PrivateKey privateKey = readPrivateKey();
            keyCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedKey = keyCipher.doFinal(Base64.getDecoder().decode(key));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(encryptedData);
            while (bis.available() > 0) {
                byte[] buffer = decrypt(iv, decryptedKey, Base64.getDecoder().decode(bis.readNBytes(7296)));
                bos.write(Base64.getDecoder().decode(buffer));
            }
            return bos.toByteArray();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new Exception("Error in decrypting content", ex);
        }
    }

    public static byte[] decrypt(byte[] iv, byte[] key, byte[] content) throws Exception {
        RijndaelEngine rijndaelEngine = new RijndaelEngine(256);
        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(rijndaelEngine), new ZeroBytePadding());
        CipherParameters cipherParametersWithIV = new ParametersWithIV(new KeyParameter(key), iv);
        cipher.init(false, cipherParametersWithIV);

        byte[] decryptedBytes = new byte[cipher.getOutputSize(content.length)];
        int processed = cipher.processBytes(content, 0, content.length, decryptedBytes, 0);
        processed += cipher.doFinal(decryptedBytes, processed);
        decryptedBytes = Arrays.copyOf(decryptedBytes, processed);

        return decryptedBytes;
    }

    public PrivateKey readPrivateKey() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        try (FileReader keyReader = new FileReader(privateKey.getFile()); PemReader pemReader = new PemReader(keyReader)) {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
            return factory.generatePrivate(privKeySpec);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("Missing or wrong Private Key", e);
        }
    }

    private HttpHeaders getRequestHeader(Object body) {
        String jsonBody = getJson(body);

        byte[] vendorAPIKeySha256 = DigestUtils.sha256(vendorAPIKey);
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, vendorAPIKeySha256);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", hm256.hmacHex(jsonBody));
        return headers;
    }

    private HttpEntity<MultiValueMap<String, Object>> getSignDocumentRequestHeader(EvrotrustSignDocumentDataRequest data,
                                                                                   EvrotrustSignDocumentDocumentRequest document) {
        byte[] vendorAPIKeySha256 = DigestUtils.sha256(vendorAPIKey);
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, vendorAPIKeySha256);

        String jsonData = getJson(data);

        ByteArrayResource documentContent = new ByteArrayResource(document.getValue()) {
            @Override
            public String getFilename() {
                return document.getOptions().getFilename();
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", hm256.hmacHex(jsonData));
        headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("data", jsonData);
        requestBody.add("document", documentContent);

        return new HttpEntity<>(requestBody, headers);
    }

    private HttpEntity<MultiValueMap<String, Object>> getSignDocumentGroupRequestHeader(Object data,
                                                                                        EvrotrustSignDocumentDocumentRequest[] documents) {
        byte[] vendorAPIKeySha256 = DigestUtils.sha256(vendorAPIKey);
        HmacUtils hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, vendorAPIKeySha256);

        String jsonData = getJson(data);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", hm256.hmacHex(jsonData));
        headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("data", jsonData);
        for (EvrotrustSignDocumentDocumentRequest document : documents) {
            ByteArrayResource resource = new ByteArrayResource(document.getValue()) {
                @Override
                public String getFilename() {
                    return document.getOptions().getFilename();
                }
            };
            requestBody.add("documents[]", resource);
        }

        return new HttpEntity<>(requestBody, headers);
    }

    private String getContentType(String filename) {
        return "application/" + filename.substring(filename.lastIndexOf(".") + 1);
    }

    private byte[] getDocument(Map<String, byte[]> map) {
        for (String key : map.keySet()) {
            if (!"OUT_SIGNED_HASH.xml".equals(key)) {
                return map.get(key);
            }
        }
        return null;
    }

    private String getJson(Object data) {
        String jsonData = null;
        try {
            jsonData = new ObjectMapper().writeValueAsString(data);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return jsonData;
    }

}
