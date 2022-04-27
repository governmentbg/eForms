package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.UniqueNumberResponse;
import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.user.model.User;

public interface OrnService {
    UniqueNumberResponse generateOrn(StartProcessInstanceRequestDto startProcessInstanceRequestDto, User user, String uriService);
}
