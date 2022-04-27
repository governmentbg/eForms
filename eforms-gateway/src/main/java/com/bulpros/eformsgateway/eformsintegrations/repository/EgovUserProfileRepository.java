package com.bulpros.eformsgateway.eformsintegrations.repository;


import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;

public interface EgovUserProfileRepository {
    UserContactData getUserProfile(String personalIdentifierNumber);
    UserContactData getUserProfile(String personalIdentifierNumber, String cacheControl);
}
