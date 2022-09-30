package com.bulpros.integrations.regix.model.MVR;

import com.bulpros.integrations.regix.model.Operation;

public enum MVROperation implements Operation {
    GЕТ_PERSONAL_IDENTITY_V3("TechnoLogica.RegiX.MVRBDSAdapter.APIService.IMVRBDSAPI.GetPersonalIdentityV3"),
    GET_MOTOR_VEHICLE_REGISTRATION_INFO("TechnoLogica.RegiX.MVRMPSAdapter.APIService.IMVRMPSAPI.GetMotorVehicleRegistrationInfo");

    private final String key;

    private MVROperation(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
