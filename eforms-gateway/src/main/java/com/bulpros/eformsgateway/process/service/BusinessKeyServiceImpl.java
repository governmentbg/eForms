package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.eformsintegrations.model.UniqueNumberResponse;
import com.bulpros.eformsgateway.eformsintegrations.service.OrnService;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.user.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessKeyServiceImpl implements BusinessKeyService {
    private final ObjectMapper objectMapper;
    private final OrnService ornService;

    private String generateBusinessKeyWithApplicantAndServiceId(String processKey, StartProcessInstanceRequestDto startProcessInstanceRequestDto) {
    String easId = "";
        try {
            String startProcessRequestJson = objectMapper.writeValueAsString(startProcessInstanceRequestDto);
            easId = JsonPath.read(startProcessRequestJson, "$.variables.context.value.service.data.arId");
        } catch (JsonProcessingException e) {
            log.error("Cannot read easID", e);
        }
        return processKey + "-" + easId + "-" + startProcessInstanceRequestDto.getApplicant();
    }

    @Override
    public String generateBusinessKey(String processKey, StartProcessInstanceRequestDto startProcessInstanceRequestDto, User user) {
        String businessKeyValue  = null;
        switch (startProcessInstanceRequestDto.getBusinessKeyType()) {
            case ORN:
                UniqueNumberResponse uniqueNumber = this.ornService.generateOrn(startProcessInstanceRequestDto, user, "e-service");
                if (uniqueNumber != null && uniqueNumber.getReturnCode().equals("200")) {
                    businessKeyValue = uniqueNumber.getOrn();
                    break;
                }
                else {
                    return null;
                }
            case GENERATED:
                businessKeyValue = this.generateBusinessKeyWithApplicantAndServiceId(processKey, startProcessInstanceRequestDto);
                break;
        }
        return businessKeyValue;
    }
}
