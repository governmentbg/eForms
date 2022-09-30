package com.bulpros.eformsgateway.eformsintegrations.repository;

import com.bulpros.eformsgateway.eformsintegrations.model.ETranslationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ETranslationRepositoryImpl extends BaseRepository implements ETranslationRepository {

    private final RestTemplate restTemplate;

    @Override
    public int postTranslationRequest(ETranslationRequest eTranslationRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ETranslationRequest> entity = new HttpEntity<>(eTranslationRequest, headers);
        int response = restTemplate
                .postForObject(getUrlTranslationRequest(), entity, Integer.class);

        return response;
    }
}
