package com.bulpros.eformsgateway.eformsintegrations.repository;


import com.bulpros.eformsgateway.eformsintegrations.model.UserAdministrationsAuthorization;

public interface EGovUserAdministrationsAuthorizationRepository {
    UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier);
    UserAdministrationsAuthorization getUserAdministrationsAuthorization(String personalIdentifier, String cacheControl);
}
