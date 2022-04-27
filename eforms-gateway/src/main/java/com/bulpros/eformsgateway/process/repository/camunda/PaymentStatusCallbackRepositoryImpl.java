package com.bulpros.eformsgateway.process.repository.camunda;

import com.bulpros.eformsgateway.process.repository.PaymentStatusCallbackRepository;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusCallbackRequestDto;
import com.bulpros.eformsgateway.process.web.dto.PaymentStatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class PaymentStatusCallbackRepositoryImpl extends BaseRepository implements PaymentStatusCallbackRepository {

    @Override
    public PaymentStatusResponseDto paymentRequestCallback(PaymentStatusCallbackRequestDto paymentStatusCallbackRequest) {
        try {
            String url = getPaymentCallbackUrl();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PaymentStatusCallbackRequestDto> entity =
                    new HttpEntity<>(paymentStatusCallbackRequest, headers);
            PaymentStatusResponseDto response = restTemplate
                    .postForObject(url, entity, PaymentStatusResponseDto.class);

            return response;
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            return new PaymentStatusResponseDto(false);
        }
    }
}
