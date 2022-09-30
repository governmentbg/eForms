package com.bulpros.integrations.evrotrust.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignDocumentsResponse {

    private EvrotrustSignDocumentResponse response;
    private Boolean groupSigning;

}
