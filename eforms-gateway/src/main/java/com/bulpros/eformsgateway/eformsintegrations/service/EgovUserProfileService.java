package com.bulpros.eformsgateway.eformsintegrations.service;


import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;

public interface EgovUserProfileService {
    UserContactData getUserProfile(String personalIdentificationNumber);
    String decryptProfileId(String base64EncryptProfileId);
}
