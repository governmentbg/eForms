package com.bulpros.eformsgateway.process.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PaymentStatusRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ClientId")
    String clientId;
    @JsonProperty("Hmac")
    String hmac;
    @JsonProperty("Data")
    String data;
}
