package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EDeliveryProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String egn;
    private String eik;
    private String name;
    private EDeliveryProfileTypeEnum profileType;
}
