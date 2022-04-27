package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.process.web.dto.PaymentStatus;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;

public interface PaymentStatusCallbackService {

    PaymentStatusResponseDto paymentRequestCallback(String processId, String message, String fieldId, PaymentStatus paymentStatus);

}
