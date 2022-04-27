package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesClassification;
import com.bulpros.eforms.processengine.camunda.model.EDeliveryFilesPackage;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.edelivery.model.ArrayOfDcDocument;
import com.bulpros.eforms.processengine.edelivery.model.DcDocument;
import com.bulpros.eforms.processengine.edelivery.model.DcMessageDetails;
import com.bulpros.eforms.processengine.minio.model.MinioFile;
import com.bulpros.eforms.processengine.minio.service.MinioService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;
import spinjar.com.fasterxml.jackson.databind.JsonNode;
import spinjar.com.fasterxml.jackson.databind.node.ArrayNode;

import java.time.LocalDateTime;
import java.util.Map;

@Setter
@Getter
@Slf4j
@Scope("prototype")
public abstract class EDeliverySend implements JavaDelegate {

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

    private Expression senderUniqueIdentifier;
    private Expression receiverUniqueIdentifier;
    private Expression eDeliveryFilesPackage;
    private Expression eDeliverySignedFilesPackage;
    private Expression eDeliveryAttachments;
    private Expression serviceOID;
    private Expression operatorEGN;
    private String senderUniqueIdentifierValue;
    private String receiverUniqueIdentifierValue;
    private String serviceOIDValue;
    private String operatorEGNValue;
    private String eDeliverySignedFilesJson;
    private DcMessageDetails message;

    protected void prepareMessage(DelegateExecution delegateExecution) throws Exception {
        if (this.getSenderUniqueIdentifier() != null) {
            senderUniqueIdentifierValue = (String) this.getSenderUniqueIdentifier().getValue(delegateExecution);
        }

        if (this.getReceiverUniqueIdentifier() != null) {
            receiverUniqueIdentifierValue = (String) this.getReceiverUniqueIdentifier().getValue(delegateExecution);
        }

        if (this.getServiceOID() != null) {
            serviceOIDValue = (String) this.getServiceOID().getValue(delegateExecution);
        }

        if (this.getOperatorEGN() != null) {
            operatorEGNValue = (String) this.getOperatorEGN().getValue(delegateExecution);
        }

        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        String projectId = (String) expressionManager.createExpression(processConfProperties.getProjectIdPathExpr())
                .getValue(delegateExecution);
        String businessKey = this.processService.getBusinessKey(delegateExecution.getProcessInstanceId());
        String arId = (String) expressionManager.createExpression(processConfProperties.getArIdPathExpr())
                .getValue(delegateExecution);
        String serviceName = (String) expressionManager.createExpression(processConfProperties.getServiceNamePathExpr())
                .getValue(delegateExecution);

        ArrayOfDcDocument arrayOfDocs = prepareDocuments(delegateExecution, projectId);

        message = new DcMessageDetails();
        message.setDateCreated(LocalDateTime.now().toString());
        String messageTitle = String.join("-", businessKey, arId, serviceName);
        messageTitle = messageTitle.length() >= 255 ? messageTitle.substring(0, 252) + "..." : messageTitle;
        message.setTitle(messageTitle);

        if (arrayOfDocs.getDcDocument().size() > 0) {
            message.setAttachedDocuments(arrayOfDocs);
        }

    }

    protected ArrayOfDcDocument prepareDocuments(DelegateExecution delegateExecution, String projectId) throws Exception {
        String eDeliveryFilesPackage = null;
        if (this.getEDeliveryFilesPackage() != null) {
            eDeliveryFilesPackage = (String) this.getEDeliveryFilesPackage().getValue(delegateExecution);
        }

        String eDeliveryAttachments = null;
        if (this.getEDeliveryAttachments() != null) {
            eDeliveryAttachments = (String) this.getEDeliveryAttachments().getValue(delegateExecution);
        }

        String eDeliverySignedFilesPackage = null;
        if (this.getEDeliverySignedFilesPackage() != null) {
            eDeliverySignedFilesPackage = (String) this.getEDeliverySignedFilesPackage().getValue(delegateExecution);
        }

        ArrayOfDcDocument arrayOfDocs = new ArrayOfDcDocument();

        if (eDeliveryFilesPackage != null) {
            processDocuments(projectId, delegateExecution.getVariable(eDeliveryFilesPackage), arrayOfDocs,
                    EDeliveryFilesClassification.ORIGINAL);
        }

        if (eDeliverySignedFilesPackage != null) {
            processDocuments(projectId, delegateExecution.getVariable(eDeliverySignedFilesPackage), arrayOfDocs,
                    EDeliveryFilesClassification.SIGNED);
        }

        if (eDeliveryAttachments != null) {
            processDocuments(projectId, delegateExecution.getVariable(eDeliveryAttachments), arrayOfDocs,
                    EDeliveryFilesClassification.ATTACHMENT);
        }

        return arrayOfDocs;
    }

    @SuppressWarnings("unchecked")
    private void processDocuments(String projectId, Object json, ArrayOfDcDocument arrayOfDocs,
                                  EDeliveryFilesClassification classification) {
        if (json instanceof JacksonJsonNode) {
            if (((JacksonJsonNode) json).unwrap() instanceof ArrayNode) {
                for (JsonNode jsonNode : ((JacksonJsonNode) json).unwrap()) {
                    getMinioDocument(projectId, jsonNode, arrayOfDocs, classification);
                }
            } else {
                getMinioDocument(projectId, json, arrayOfDocs, classification);
            }
        }
    }

    private void getMinioDocument(String projectId, Object json, ArrayOfDcDocument arrayOfDocs,
                                  EDeliveryFilesClassification classification) {
        EDeliveryFilesPackage eDeliveryFilesPackage = Spin.JSON(json).mapTo(EDeliveryFilesPackage.class);
        classification.processableFiles(eDeliveryFilesPackage)
                .forEach(file -> addMinioDocument(projectId, file, arrayOfDocs));
    }

    private void addMinioDocument(String projectId, MinioFile documentDto, ArrayOfDcDocument arrayOfDocs) {
        try {
            MinioFile documentMinio = this.minioService.getFile(projectId, documentDto, true);

            if (documentMinio != null) {
                DcDocument dcDocument = new DcDocument();
                dcDocument.setDocumentName(documentMinio.getFilename());
                dcDocument.setContentType(documentMinio.getContentType());
                dcDocument.setContent(documentMinio.getContent());
                dcDocument.setId(arrayOfDocs.getDcDocument().size());
                arrayOfDocs.getDcDocument().add(dcDocument);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
