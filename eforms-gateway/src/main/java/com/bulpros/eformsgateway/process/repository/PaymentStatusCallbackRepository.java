package com.bulpros.eformsgateway.process.repository;

import com.bulpros.eformsgateway.process.web.dto.PaymentStatusCallbackRequestDto;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;

public interface PaymentStatusCallbackRepository {

    PaymentStatusResponseDto paymentRequestCallback(PaymentStatusCallbackRequestDto paymentStatusCallbackRequest);

}
