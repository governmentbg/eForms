package com.bulpros.eformsgateway.eformsintegrations.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UniqueNumberResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String returnCode;
    private String descriptionCode;
    private String orn;
    private String generatedAt;

    public String getOrnWithTimestamp() {
        return this.orn + " " + this.generatedAt;
    }
}
