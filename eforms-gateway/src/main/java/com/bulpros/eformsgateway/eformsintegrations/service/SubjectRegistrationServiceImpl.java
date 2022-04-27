package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryRegistration;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.SubjectRegistrationResponse;
import com.bulpros.eformsgateway.eformsintegrations.repository.SubjectRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectRegistrationServiceImpl implements SubjectRegistrationService {

    private final SubjectRegistrationRepository subjectRegistrationRepository;

    @Override
    public CheckEDeliveryRegistrationResult checkIfSubjectHasRegistration(String identifier) {
        try {
            SubjectRegistrationResponse subjectRegistrationResponse = subjectRegistrationRepository.getSubjectRegistrationResponse(identifier);
            if (subjectRegistrationResponse.getHasRegistration()) {
                return new CheckEDeliveryRegistrationResult(
                        new EDeliveryRegistration(subjectRegistrationResponse.getIdentificator(),
                                subjectRegistrationResponse.getSubjectInfo().getProfileType(), null),
                        EDeliveryStatusEnum.OK);
            }
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.PROFILE_NOT_FOUND);
        } catch (Exception e) {
            return new CheckEDeliveryRegistrationResult(null, EDeliveryStatusEnum.SERVICE_NOT_AVAILABLE);
        }
    }
}
