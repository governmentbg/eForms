package com.bulpros.eformsgateway.eformsintegrations.service;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryProfileTypeEnum;
import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.user.model.User;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static com.bulpros.eformsgateway.form.utils.ValidationUtils.isNotPresent;

@Service
@RequiredArgsConstructor
public class EDeliveryRegistrationServiceImpl implements EDeliveryRegistrationService {

    private final SubjectRegistrationService subjectRegistrationService;
    private final LegalPersonRegistrationService legalPersonRegistrationService;
    private final PersonRegistrationService personRegistrationService;
    private final UserProfileService userProfileService;

    @Timed(value = "eforms-gateway-check-registration.time")
    @Override
    public CheckEDeliveryRegistrationResult checkRegistration(User user, Authentication authentication, String projectId, String applicant) {
        CheckEDeliveryRegistrationResult checkEDeliveryRegistrationResult;
        if (isNotPresent(applicant)) {
            checkEDeliveryRegistrationResult = subjectRegistrationService.checkIfSubjectHasRegistration(user.getUcn());
        } else {
            EDeliveryProfileTypeEnum applicantProfileType =
                    userProfileService.getEDeliveryProfileTypeByApplicant(projectId, user.getPersonIdentifier(), authentication, applicant);
            if (applicantProfileType.equals(EDeliveryProfileTypeEnum.LEGAL_PERSON)) {
                checkEDeliveryRegistrationResult = legalPersonRegistrationService.checkIfLegalPersonHasRegistration(applicant);
                if (checkEDeliveryRegistrationResult.getStatus().equals(EDeliveryStatusEnum.OK)) {
                    checkEDeliveryRegistrationResult = personRegistrationService.checkIfPersonHasProfileAccess(user.getUcn(), applicant, applicantProfileType);
                }
            } else
                checkEDeliveryRegistrationResult = personRegistrationService.checkIfPersonHasProfileAccess(user.getUcn(), applicant, applicantProfileType);
        }
        return checkEDeliveryRegistrationResult;
    }
}
