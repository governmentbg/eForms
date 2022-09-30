package com.bulpros.eformsgateway.process.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    String id;
    @JsonProperty("Status")
    String status;
    @JsonProperty("ChangeTime")
    String changeTime;
}
