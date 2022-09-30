package com.bulpros.integrations.ePayment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentClientDetailsResponse {

        private String departmentId;
        private String name;
        private String uniqueIdentificationNumber;
        private Boolean isActive;
        private String eserviceClientId;
        private String aisName;
        private String accountBank;
        private String accountBIC;
        private String accountIBAN;

}
