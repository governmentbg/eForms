package com.bulpros.eforms.processengine.egov.model.eservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IdentificationMethods {
    private List<String> identificationMethod;
}
