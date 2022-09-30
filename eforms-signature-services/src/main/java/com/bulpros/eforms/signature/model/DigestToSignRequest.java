package com.bulpros.eforms.signature.model;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
public class DigestToSignRequest extends DataToSignRequest {
    
    @NotNull
    private String documentName;
    
    @NotNull
    private String digestToSign;
}
