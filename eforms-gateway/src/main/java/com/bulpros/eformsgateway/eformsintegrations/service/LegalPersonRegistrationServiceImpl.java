package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryRegistration;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.LegalPersonRegistrationResponse;
import com.bulpros.eformsgateway.eformsintegrations.repository.LegalPersonRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LegalPersonRegistrationServiceImpl implements LegalPersonRegistrationService {

    private final LegalPersonRegistrationRepository legalPersonRegistrationRepository;

    @Override
    public CheckEDeliveryRegistrationResult checkIfLegalPersonHasRegistration(String eik) {
        try {
            LegalPersonRegistrationResponse legalPersonRegistrationResponse = legalPersonRegistrationRepository.getLegalPersonRegistrationResponse(eik);
            if (legalPersonRegistrationResponse.getHasRegistration()) {
                return new CheckEDeliveryRegistrationResult(new EDeliveryRegistration(), EDeliveryStatusEnum.OK);
            }
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.PROFILE_NOT_FOUND);
        } catch (Exception e) {
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.SERVICE_NOT_AVAILABLE);
        }
    }
}
