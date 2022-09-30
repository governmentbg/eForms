package com.bulpros.eformsgateway.eformsintegrations.repository;


import com.bulpros.eformsgateway.eformsintegrations.model.ETranslationRequest;

public interface ETranslationRepository {
    int postTranslationRequest(ETranslationRequest eTranslationRequest);
}
