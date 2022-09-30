package com.bulpros.eformsgateway.process.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetadataDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Object> commonNomEasSuppliers;
    private HashMap<String, Object> commonResourceTermTaxes;
}
