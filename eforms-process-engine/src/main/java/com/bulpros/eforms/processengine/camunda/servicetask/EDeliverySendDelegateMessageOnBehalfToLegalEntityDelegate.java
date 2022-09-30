package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToLegalEntityRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendMessageOnBehalfToLegalEntityResponse;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Slf4j
@Setter
@Getter
@Named("eDeliverySendMessageOnBehalfToLegalEntity")
@Scope("prototype")
public class EDeliverySendDelegateMessageOnBehalfToLegalEntityDelegate extends EDeliverySendDelegate {

    @Autowired
    public EDeliverySendDelegateMessageOnBehalfToLegalEntityDelegate(MinioService minioService,
                                                                     CamundaProcessRepository processService,
                                                                     ConfigurationProperties processConfProperties,
                                                                     RestTemplate restTemplate) {
        super(minioService, processService, processConfProperties, restTemplate);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        prepareMessage(delegateExecution);

        SendMessageOnBehalfToLegalEntityRequest sendMessageOnBehalfToLegalEntityRequest = new SendMessageOnBehalfToLegalEntityRequest();
        sendMessageOnBehalfToLegalEntityRequest.setMessage(getMessage());
        sendMessageOnBehalfToLegalEntityRequest.setSenderUniqueIdentifier(getSenderUniqueIdentifierValue());
        sendMessageOnBehalfToLegalEntityRequest.setReceiverUniqueIdentifier(getReceiverUniqueIdentifierValue());
        sendMessageOnBehalfToLegalEntityRequest.setOperatorEGN(getOperatorEGNValue());
        sendMessageOnBehalfToLegalEntityRequest.setServiceOID(getServiceOIDValue());

        try {
            SendMessageOnBehalfToLegalEntityResponse response = getRestTemplate().postForObject(
                    UriComponentsBuilder.fromHttpUrl(this.getEDeliveryUrl() + this.getEDeliveryPath())
                            .path("/send-message-on-behalf-to-legal-entity").toUriString(),
                    sendMessageOnBehalfToLegalEntityRequest, SendMessageOnBehalfToLegalEntityResponse.class);

            if (response != null && response.getSendMessageOnBehalfToLegalEntityResult() != null) {
                delegateExecution.setVariable("eDeliverySendMessageOnBehalfToLegalEntityResult", response.getSendMessageOnBehalfToLegalEntityResult());
            } else {
                delegateExecution.setVariable("eDeliverySendMessageOnBehalfToLegalEntityResult", null);
            }
        } catch (RestClientResponseException exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        } catch (RestClientException exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "INTEGRATIONS.UNAVAILABLE", exception.getMessage());
        }

    }
}
