package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.process.web.dto.PaymentStatusEnum;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;

import java.util.Date;

public interface PaymentStatusCallbackService {

    PaymentStatusResponseDto paymentRequestCallback(String processId, String message, String fieldId,
                                                    String paymentId, PaymentStatusEnum paymentStatus,
                                                    Date paymentChangeTime);

}
