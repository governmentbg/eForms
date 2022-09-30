package com.bulpros.integrations.ePayment.service;

import com.bulpros.integrations.ePayment.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component("ePaymentService")
@Slf4j
@RequiredArgsConstructor
public class EPaymentService {

    private final RestTemplate restTemplateEsb;
    private final EPaymentTokenManager tokenManager;


    @Value("${com.bulpros.ePayment.register.payment.extended.url}")
    private String registerPaymentExtendedUrl;
    @Value("${com.bulpros.ePayment.payment.status.url}")
    private String paymentsStatusUrl;
    @Value("${com.bulpros.ePayment.change.payment.status.url}")
    private String changePaymentStatusUrl;
    @Value("${com.bulpros.ePayment.aisClientId}")
    private String aisClientId;
    @Value("${com.bulpros.ePayment.aisSecretKey}")
    private String aisSecretKey;

    public RegisterPaymentResponse registerPaymentExtended(PaymentRequest paymentRequest) {
        String token = tokenManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.set("Service-Cid", paymentRequest.getEServiceClientId());
        ResponseEntity<RegisterPaymentResponse> response =
                restTemplateEsb.postForEntity(registerPaymentExtendedUrl,
                        new HttpEntity<>(paymentRequest.getPaymentRequest(), headers), RegisterPaymentResponse.class);

        return response.getBody();
    }


    public PaymentStatusResponse paymentStatus(String paymentId) {
        UriComponentsBuilder request = UriComponentsBuilder.fromHttpUrl(paymentsStatusUrl)
                .queryParam("paymentId", paymentId);
        String token = tokenManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        ResponseEntity<PaymentStatusResponse> response =
                restTemplateEsb.exchange(request.toUriString(), HttpMethod.GET,
                        new HttpEntity<>(headers), PaymentStatusResponse.class);

        return response.getBody();
    }

    public RegisterPaymentResponse changePaymentStatus(ChangePaymentStatusRequest changePaymentStatusRequest) {
        String token = tokenManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        ResponseEntity<RegisterPaymentResponse> response =
                restTemplateEsb.exchange(changePaymentStatusUrl, HttpMethod.PUT,
                        new HttpEntity<>(changePaymentStatusRequest, headers), RegisterPaymentResponse.class);

        return response.getBody();
    }

    public PayWithBoricaResponse payWithBorica(PayWithBoricaRequest paymentWithBoricaRequest) throws Exception {
        String data = prepareData(paymentWithBoricaRequest);
        String hmac = prepareHmac(data);
        return new PayWithBoricaResponse(aisClientId, data, hmac);
    }

    private HttpEntity<MultiValueMap<String, String>> prepareRequest(Object content, String eServiceClientId) throws Exception {
        String data = prepareData(content);
        String hmac = prepareHmac(data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("clientId", aisClientId);
        requestBody.add("data", data);
        requestBody.add("hmac", hmac);
        if (eServiceClientId != null && !eServiceClientId.isEmpty()) {
            requestBody.add("eServiceClientId", eServiceClientId);
        }

        return new HttpEntity<>(requestBody, headers);
    }

    private String prepareData(Object content) throws Exception {
        String message = new ObjectMapper().writeValueAsString(content);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(messageBytes);
    }

    private String prepareHmac(String data) throws Exception {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(aisSecretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secret_key);
        return Base64.getEncoder().encodeToString(hmacSha256.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }
}
