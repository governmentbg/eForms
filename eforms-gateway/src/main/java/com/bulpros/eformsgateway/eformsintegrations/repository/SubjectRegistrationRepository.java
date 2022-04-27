package com.bulpros.eformsgateway.eformsintegrations.repository;

import com.bulpros.eformsgateway.eformsintegrations.model.SubjectRegistrationResponse;

public interface SubjectRegistrationRepository {
    SubjectRegistrationResponse getSubjectRegistrationResponse(String identifier);
    SubjectRegistrationResponse getSubjectRegistrationResponse(String identifier, String cacheControl);
}
