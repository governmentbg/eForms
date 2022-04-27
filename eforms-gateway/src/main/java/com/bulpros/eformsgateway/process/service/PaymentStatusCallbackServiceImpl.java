package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.process.repository.PaymentStatusCallbackRepository;
import com.bulpros.eformsgateway.process.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusCallbackServiceImpl implements PaymentStatusCallbackService {

    private final PaymentStatusCallbackRepository paymentStatusCallbackRepository;

    @Override
    public PaymentStatusResponseDto paymentRequestCallback(String processId, String message, String fieldId, PaymentStatus paymentStatus) {

        return paymentStatusCallbackRepository.paymentRequestCallback(
                    new PaymentStatusCallbackRequestDto(paymentStatus.getId(),
                            paymentStatus.getStatus(), paymentStatus.getChangeTime(),
                            processId, message, fieldId));
    }
}
