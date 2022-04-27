package com.bulpros.eformsgateway.eformsintegrations.service;


import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;

public interface EGovUserAdministrationsAuthorizationService {

    UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier);
}
