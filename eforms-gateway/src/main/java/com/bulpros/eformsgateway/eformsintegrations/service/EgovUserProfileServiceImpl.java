package com.bulpros.eformsgateway.eformsintegrations.service;


import com.bulpros.eformsgateway.eformsintegrations.model.UserContactData;
import com.bulpros.eformsgateway.eformsintegrations.repository.EgovUserProfileRepository;
import com.bulpros.eformsgateway.eformsintegrations.utils.AES_GCM_Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class EgovUserProfileServiceImpl implements EgovUserProfileService {

    @Value("${com.bulpros.egov.profileId.secret-key}")
    private String profileIdSecretKey;

    private final EgovUserProfileRepository egovRepository;

    @Override
    public UserContactData getUserProfile(String personalIdentificationNumber) {
        return egovRepository.getUserProfile(personalIdentificationNumber);
    }

    @Override
    public String decryptProfileId(String base64EncryptProfileId) {
        byte[] encryptedText = Base64.getDecoder().decode(base64EncryptProfileId);
        byte[] secretKey = Base64.getDecoder().decode(profileIdSecretKey);
        try {
            return AES_GCM_Utils.decrypt(encryptedText, secretKey);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
