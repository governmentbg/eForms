package com.bulpros.integrations.eTranslation.service;

import com.bulpros.integrations.eTranslation.model.CallerInformation;
import com.bulpros.integrations.eTranslation.model.TranslationRequest;
import com.bulpros.integrations.exceptions.NotSuccessTranslationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component("eTranslationService")
@RequiredArgsConstructor
public class ETranslationService {
    @Value("${com.bulpros.etranslation.application}")
    private String eTranslationApplication;

    @Value("${com.bulpros.etranslation.username}")
    private String eTranslationUsername;

    @Qualifier("eTranslationHttpClient")
    private final CloseableHttpClient httpClient;

    private final ObjectMapper objectMapper;
    private final HttpPost httpPost;
    @SneakyThrows
    public int translate(TranslationRequest translationRequest) {
        translationRequest.setCallerInformation(
                new CallerInformation(eTranslationApplication, eTranslationUsername));
        httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(translationRequest),
                ContentType.APPLICATION_JSON));
        HttpResponse response = httpClient.execute(httpPost);
        var result = EntityUtils.toString(response.getEntity());
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            throw  new NotSuccessTranslationException("Wrong request format. Reason: "
            + result);
        }
    }
}
