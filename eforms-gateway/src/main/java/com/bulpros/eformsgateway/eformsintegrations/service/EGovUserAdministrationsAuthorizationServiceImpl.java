package com.bulpros.eformsgateway.eformsintegrations.service;


import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;
import com.bulpros.eformsgateway.eformsintegrations.repository.EGovUserAdministrationsAuthorizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EGovUserAdministrationsAuthorizationServiceImpl implements EGovUserAdministrationsAuthorizationService {

    private final EGovUserAdministrationsAuthorizationRepository eGovUserAdministrationsAuthorizationRepository;

    @Override
    public UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier) {
        return eGovUserAdministrationsAuthorizationRepository.getUserAdministrationsAuthorization(personalIdentifier);
    }
}
