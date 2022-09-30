package com.bulpros.integrations.orn.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UniqueNumberResponse {

    private String orn;
    private String returnCode;
    private String descriptionCode;
    private String generatedAt;

}
