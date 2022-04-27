package com.bulpros.eformsgateway.eformsintegrations.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CheckEDeliveryRegistrationResult {

    private EDeliveryRegistration profile;
    private EDeliveryStatusEnum status;
}
