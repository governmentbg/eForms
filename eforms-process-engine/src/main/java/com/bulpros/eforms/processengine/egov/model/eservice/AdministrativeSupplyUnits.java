package com.bulpros.eforms.processengine.egov.model.eservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdministrativeSupplyUnits {
    private List<AdministrativeSupplyUnit> administrativeSupplyUnit;
}
