package com.bulpros.integrations.orn.service;

import com.bulpros.integrations.orn.model.UniqueNumberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.nonNull;


@Component("ornService")
@RequiredArgsConstructor
@Slf4j
public class OrnService {

    private final RestTemplate restTemplateEsb;
    private final OrnTokenManager tokenManager;

    @Value("${com.bulpros.orn.url}")
    private String ornUrl;

    public UniqueNumberResponse getUniqueNumber(String bulstat, String typeService, String uriService) {

        UriComponentsBuilder request = UriComponentsBuilder.fromHttpUrl(ornUrl)
                .queryParam("bulstat", bulstat)
                .queryParam("type_service", typeService)
                .queryParam("uri_service", uriService);

        ResponseEntity<UniqueNumberResponse> responseEntity = this.restTemplateEsb.exchange(request.toUriString(),
                HttpMethod.GET, prepareRequest(), UniqueNumberResponse.class);

        UniqueNumberResponse uniqueNumberResponse = responseEntity.getBody();
        if (nonNull(uniqueNumberResponse)) {
            uniqueNumberResponse.setReturnCode(String.valueOf(responseEntity.getStatusCodeValue()));
            uniqueNumberResponse.setDescriptionCode(responseEntity.getStatusCode().name());
            uniqueNumberResponse.setGeneratedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        }
        return uniqueNumberResponse;
    }

    private HttpEntity<Void> prepareRequest() {
        String token = tokenManager.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return new HttpEntity<>(headers);
    }
}
