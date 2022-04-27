package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.*;
import com.bulpros.eformsgateway.eformsintegrations.repository.PersonRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonRegistrationServiceImpl implements PersonRegistrationService {

    private final PersonRegistrationRepository personRegistrationRepository;

    @Override
    public CheckEDeliveryRegistrationResult checkIfPersonHasProfileAccess(String identifier, String identification, EDeliveryProfileTypeEnum profileType) {
        try {
            PersonRegistrationResponse personRegistrationResponse = personRegistrationRepository.getPersonRegistrationResponse(identifier);
            if (personRegistrationResponse.getHasRegistration()) {
                EDeliveryProfile eDeliveryProfile = profileType.hasAccess(personRegistrationResponse.getProfiles(), identification);
                if (eDeliveryProfile != null) {
                    return new CheckEDeliveryRegistrationResult(
                            new EDeliveryRegistration(personRegistrationResponse.getPersonIdentificator(),
                                    null,
                                    eDeliveryProfile), EDeliveryStatusEnum.OK);
                }
            }
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.NOT_AUTHORIZED);
        } catch (Exception e) {
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.SERVICE_NOT_AVAILABLE);
        }
    }
}
