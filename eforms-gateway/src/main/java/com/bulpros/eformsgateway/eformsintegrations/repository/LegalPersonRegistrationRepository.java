package com.bulpros.eformsgateway.eformsintegrations.repository;

import com.bulpros.eformsgateway.eformsintegrations.model.LegalPersonRegistrationResponse;

public interface LegalPersonRegistrationRepository {
    LegalPersonRegistrationResponse getLegalPersonRegistrationResponse(String eik);
    LegalPersonRegistrationResponse getLegalPersonRegistrationResponse(String eik, String cacheControl);
}
