package com.bulpros.eformsgateway.eformsintegrations.repository;

import com.bulpros.eformsgateway.eformsintegrations.model.UniqueNumberResponse;

public interface OrnRepository {
    UniqueNumberResponse generateOrn(String typeService, String bulstat, String uriService);

}
