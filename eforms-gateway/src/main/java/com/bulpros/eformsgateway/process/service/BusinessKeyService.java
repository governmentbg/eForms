package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.process.web.dto.StartProcessInstanceRequestDto;
import com.bulpros.eformsgateway.user.model.User;

public interface BusinessKeyService {
    String generateBusinessKey(String processKey, StartProcessInstanceRequestDto startProcessInstanceRequestDto, User user);
}
