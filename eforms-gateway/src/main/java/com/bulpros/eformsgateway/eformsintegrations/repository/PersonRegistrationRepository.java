package com.bulpros.eformsgateway.eformsintegrations.repository;

import com.bulpros.eformsgateway.eformsintegrations.model.PersonRegistrationResponse;

public interface PersonRegistrationRepository {
    PersonRegistrationResponse getPersonRegistrationResponse(String personIdentificator);
    PersonRegistrationResponse getPersonRegistrationResponse(String personIdentificator, String cacheControl);
}
