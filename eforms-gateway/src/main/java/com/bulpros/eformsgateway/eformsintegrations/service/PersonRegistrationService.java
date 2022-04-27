package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryProfileTypeEnum;

public interface PersonRegistrationService {
    CheckEDeliveryRegistrationResult checkIfPersonHasProfileAccess(String identifier, String identification, EDeliveryProfileTypeEnum profileType);
}
