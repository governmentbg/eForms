package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.EProfileType;
import com.bulpros.eforms.processengine.edelivery.model.SendElectronicDocumentOnBehalfOfRequest;
import com.bulpros.eforms.processengine.edelivery.model.SendElectronicDocumentResponse;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Named;

@Setter
@Getter
@Named("eDeliverySendElectronicDocumentOnBehalfOf")
public class EDeliverySendElectronicDocumentOnBehalfOf implements JavaDelegate {

    @Value("${com.bulpros.eforms-integrations.url}")
    private String eDeliveryUrl;
    @Value("${com.bulpros.eforms-integrations.edelivery.prefix}")
    private String eDeliveryPath;

    @Autowired
    private MinioService minioService;
    @Autowired
    private CamundaProcessRepository processService;
    @Autowired
    protected ConfigurationProperties processConfProperties;
    @Autowired
    private RestTemplate restTemplate;

    // From where
    private Expression subject;
    private Expression docNameWithExtension;
    private Expression docRegNumber;
    private Expression senderType;
    private Expression senderUniqueIdentifier;
    private Expression senderPhone;
    private Expression senderEmail;
    private Expression senderFirstName;
    private Expression senderLastName;
    private Expression receiverType;
    private Expression receiverUniqueIdentifier;
    private Expression serviceOID;
    private Expression operatorEGN;
    private Expression eDeliveryPdf;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String subject = (String) this.getSubject().getValue(delegateExecution);
        String docNameWithExtension = (String) this.getDocNameWithExtension().getValue(delegateExecution);
        String docRegNumber = (String) this.getDocRegNumber().getValue(delegateExecution);
        String senderType = (String) this.getSenderType().getValue(delegateExecution);
        String senderUniqueIdentifier = (String) this.getSenderUniqueIdentifier().getValue(delegateExecution);
        String senderPhone = (String) this.getSenderPhone().getValue(delegateExecution);
        String senderEmail = (String) this.getSenderEmail().getValue(delegateExecution);
        String senderFirstName = (String) this.getSenderFirstName().getValue(delegateExecution);
        String senderLastName = (String) this.getSenderLastName().getValue(delegateExecution);
        String receiverType = (String) this.getReceiverType().getValue(delegateExecution);
        String receiverUniqueIdentifier = (String) this.getReceiverUniqueIdentifier().getValue(delegateExecution);
        String serviceOID = (String) this.getServiceOID().getValue(delegateExecution);
        String operatorEGN = (String) this.getOperatorEGN().getValue(delegateExecution);
        String eDeliveryPdf = (String) this.getEDeliveryPdf().getValue(delegateExecution);

        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                .getValue(delegateExecution);

        MinioFile eDeliveryPdfDto = Spin.JSON(delegateExecution.getVariable(eDeliveryPdf))
                .mapTo(MinioFile.class);

        MinioFile eDeliveryPdfMinIO = this.minioService.getFile(projectId, eDeliveryPdfDto);

        SendElectronicDocumentOnBehalfOfRequest sendElectronicDocumentOnBehalfOfRequest = new SendElectronicDocumentOnBehalfOfRequest();
        sendElectronicDocumentOnBehalfOfRequest.setSubject(subject);
        sendElectronicDocumentOnBehalfOfRequest.setDocBytes(eDeliveryPdfMinIO.getContent());
        sendElectronicDocumentOnBehalfOfRequest.setDocNameWithExtension(docNameWithExtension);
        sendElectronicDocumentOnBehalfOfRequest.setDocRegNumber(docRegNumber);
        sendElectronicDocumentOnBehalfOfRequest.setSenderType(EProfileType.fromValue(senderType));
        sendElectronicDocumentOnBehalfOfRequest.setSenderUniqueIdentifier(senderUniqueIdentifier);
        sendElectronicDocumentOnBehalfOfRequest.setSenderPhone(senderPhone);
        sendElectronicDocumentOnBehalfOfRequest.setSenderEmail(senderEmail);
        sendElectronicDocumentOnBehalfOfRequest.setSenderFirstName(senderFirstName);
        sendElectronicDocumentOnBehalfOfRequest.setSenderLastName(senderLastName);
        sendElectronicDocumentOnBehalfOfRequest.setReceiverType(EProfileType.fromValue(receiverType));
        sendElectronicDocumentOnBehalfOfRequest.setReceiverUniqueIdentifier(receiverUniqueIdentifier);
        sendElectronicDocumentOnBehalfOfRequest.setServiceOID(serviceOID);
        sendElectronicDocumentOnBehalfOfRequest.setOperatorEGN(operatorEGN);

        SendElectronicDocumentResponse response = restTemplate.postForObject(
                UriComponentsBuilder.fromHttpUrl(this.eDeliveryUrl + this.eDeliveryPath)
                        .path("/send-electronic-document-on-behalf-of").toUriString(),
                sendElectronicDocumentOnBehalfOfRequest, SendElectronicDocumentResponse.class);

        if (response != null && response.getSendElectronicDocumentResult() != null) {
            delegateExecution.setVariable("eDeliverySendMessageResult", response.getSendElectronicDocumentResult());
        } else {
            delegateExecution.setVariable("eDeliverySendMessageResult", null);
        }

    }

}
