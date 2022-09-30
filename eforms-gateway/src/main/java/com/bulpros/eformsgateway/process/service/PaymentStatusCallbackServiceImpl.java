package com.bulpros.eformsgateway.process.service;

import com.bulpros.eformsgateway.process.repository.PaymentStatusCallbackRepository;
import com.bulpros.eformsgateway.process.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentStatusCallbackServiceImpl implements PaymentStatusCallbackService {

    private final PaymentStatusCallbackRepository paymentStatusCallbackRepository;

    @Override
    public PaymentStatusResponseDto paymentRequestCallback(String processId, String message, String fieldId,
                                                           String paymentId, PaymentStatusEnum paymentStatus,
                                                           Date paymentChangeTime) {

        return paymentStatusCallbackRepository.paymentRequestCallback(
                    new PaymentStatusCallbackRequestDto(paymentId, paymentStatus, paymentChangeTime,
                            processId, message, fieldId));
    }
}
