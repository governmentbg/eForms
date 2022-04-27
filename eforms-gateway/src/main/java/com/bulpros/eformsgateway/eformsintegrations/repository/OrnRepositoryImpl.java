package com.bulpros.eformsgateway.eformsintegrations.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.bulpros.eformsgateway.eformsintegrations.model.UniqueNumberResponse;

@Component
@Slf4j
public class OrnRepositoryImpl extends BaseRepository implements OrnRepository {

    @Override
    public UniqueNumberResponse generateOrn(String typeService, String bulstat, String uriService) {
        UriComponentsBuilder request = UriComponentsBuilder.fromHttpUrl(getOrnUrl())
                .path("/get-unique-number")
                .queryParam("typeService", typeService)
                .queryParam("bulstat", bulstat)
                .queryParam("uriService", uriService);
        var responseEntity = restTemplate.getForEntity(request.toUriString(), UniqueNumberResponse.class);
        return responseEntity.getBody();
    }

}
