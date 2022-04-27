package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.exception.ServiceNotAvailableException;
import com.bulpros.eformsgateway.eformsintegrations.model.UniqueNumberResponse;
import com.bulpros.eformsgateway.eformsintegrations.repository.OrnRepository;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.user.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrnServiceImpl implements OrnService {

    private final OrnRepository ornRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UniqueNumberResponse generateOrn(StartProcessInstanceRequestDto startProcessInstanceRequestDto, User user, String uriService) {
        try {
            String startProcessRequestJson = objectMapper.writeValueAsString(startProcessInstanceRequestDto);

            String easId = JsonPath.read(startProcessRequestJson, "$.variables.context.value.service.data.arId");

            UniqueNumberResponse orn = ornRepository.generateOrn(easId, user.getUcn(), uriService);
            if (!HttpStatus.OK.name().equals(orn.getDescriptionCode()) || orn.getOrn() == null || orn.getOrn().isEmpty()) {
                throw new ServiceNotAvailableException(ServiceNotAvailableException.ORN_NOT_AVAILABLE);
            }

            return orn;
        } catch (JsonProcessingException jpe) {
            log.error("Cannot read easID", jpe);
        } catch (Exception e) {
            throw new ServiceNotAvailableException(ServiceNotAvailableException.ORN_NOT_AVAILABLE);
        }
        return null;
    }
}
