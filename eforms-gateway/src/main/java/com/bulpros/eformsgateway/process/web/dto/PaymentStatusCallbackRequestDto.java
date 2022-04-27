package com.bulpros.eformsgateway.process.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusCallbackRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    String id;
    PaymentStatusEnum status;
    Date changeTime;

    String processId;
    String message;
    String fieldId;
}
