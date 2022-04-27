package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.*;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.spin.Spin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;
import java.time.LocalDateTime;

@Setter
@Getter
@Named("eDeliverySendMessage")
@Scope("prototype")
public class EDeliverySendMessage extends EDeliverySend {
    @Value("${com.bulpros.eforms-integrations.url}")
    private String eDeliveryUrl;
    @Value("${com.bulpros.eforms-integrations.edelivery.prefix}")
    private String eDeliveryPath;

    @Autowired
    private CamundaProcessRepository processService;
    @Autowired
    protected ConfigurationProperties processConfProperties;
    @Autowired
    private MinioService minioService;
    @Autowired
    private RestTemplate restTemplate;

    private Expression receiverType;
    private Expression receiverUniqueIdentifier;
    private Expression serviceOID;
    private Expression operatorEGN;
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String receiverType = (String) this.getReceiverType().getValue(execution);
        String receiverUniqueIdentifier = (String) this.getReceiverUniqueIdentifier().getValue(execution);
        String serviceOID = (String) this.getServiceOID().getValue(execution);
        String operatorEGN = null;
        if(this.getOperatorEGN() != null) {
            operatorEGN = (String) this.getOperatorEGN().getValue(execution);
        }

        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                .getValue(execution);
        String businessKey = this.processService.getBusinessKey(execution.getProcessInstanceId());
        String arId = (String) expressionManager.createExpression(processConfProperties.getArIdPathExpr())
                .getValue(execution);
        String serviceName = (String) expressionManager.createExpression(processConfProperties.getServiceNamePathExpr())
                .getValue(execution);

        ArrayOfDcDocument arrayOfDocs = prepareDocuments(execution, projectId);

        DcMessageDetails messageDetails = new DcMessageDetails();
        messageDetails.setDateCreated(LocalDateTime.now().toString());
        messageDetails.setTitle(String.join("-", arId, serviceName, businessKey));
        if (arrayOfDocs.getDcDocument().size() > 0){
            messageDetails.setAttachedDocuments(arrayOfDocs);
        }

        SendMessageRequest sendMessageRequest = new SendMessageRequest();
        sendMessageRequest.setMessageDetails(messageDetails);
        sendMessageRequest.setReceiverType(EProfileType.fromValue(receiverType));
        sendMessageRequest.setReceiverUniqueIdentifier(receiverUniqueIdentifier);
        sendMessageRequest.setServiceOID(serviceOID);
        if(operatorEGN != null) {
            sendMessageRequest.setOperatorEGN(operatorEGN);
        }

        SendMessageResponse response = restTemplate.postForObject(
                UriComponentsBuilder.fromHttpUrl(this.eDeliveryUrl + this.eDeliveryPath)
                        .path("/send-message").toUriString(),
                sendMessageRequest, SendMessageResponse.class);

        if (response != null && response.getSendMessageResult() != null) {
            execution.setVariable("eDeliverySendMessageResult", response.getSendMessageResult());
        } else {
            execution.setVariable("eDeliverySendMessageResult", null);
        }

    }
}
