package com.bulpros.integrations.egov.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserContactDataResponse {
    private String result;
    private Profile profile;
    private String message;
}
