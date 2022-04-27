package com.bulpros.eformsgateway.eformsintegrations.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EDeliveryRegistration {

    private String requestorIdentification;
    private String requestorProfileType;
    private EDeliveryProfile applicantProfile;

}
