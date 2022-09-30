package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.ETranslationRequest;
import com.bulpros.eformsgateway.eformsintegrations.repository.ETranslationRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ETranslationServiceImpl implements ETranslationService {

    private final ETranslationRepositoryImpl eTranslationRepositoryImpl;

    @Override
    public int sendTranslationRequest(ETranslationRequest eTranslationRequest) {
        return eTranslationRepositoryImpl.postTranslationRequest(eTranslationRequest);
    }
}
