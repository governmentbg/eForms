package com.bulpros.eformsgateway.process.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class PaymentStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    String id;
    PaymentStatusEnum status;
    Date changeTime;

}
