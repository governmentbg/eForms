package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;

public interface SubjectRegistrationService {
    CheckEDeliveryRegistrationResult checkIfSubjectHasRegistration(String identifier);
}
