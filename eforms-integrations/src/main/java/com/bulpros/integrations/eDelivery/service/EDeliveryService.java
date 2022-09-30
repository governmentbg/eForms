package com.bulpros.integrations.eDelivery.service;

import com.bulpros.integrations.eDelivery.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("eDeliveryService")
@Slf4j
public class EDeliveryService {


    private final EDeliveryClient eDeliveryClient;

    public EDeliveryService(EDeliveryClient eDeliveryClient) {
        this.eDeliveryClient = eDeliveryClient;
    }

    public DcSubjectRegistrationInfo checkSubjectHasRegistration(String identifier) {
        return eDeliveryClient.geteDelivery().checkSubjectHasRegistration(identifier);
    }

    public DcPersonRegistrationInfo checkPersonHasRegistration(String personIdentificator) {
        return eDeliveryClient.geteDelivery().checkPersonHasRegistration(personIdentificator);
    }

    public DcLegalPersonRegistrationInfo checkLegalPersonHasRegistration(String identificator) {
        return eDeliveryClient.geteDelivery().checkLegalPersonHasRegistration(identificator);
    }

    public SendMessageResponse sendMessage(SendMessageRequest sendMessageRequest) {
        SendMessageResponse sendMessageResponse = new SendMessageResponse();
        Integer result = eDeliveryClient.geteDelivery().sendMessage(sendMessageRequest.getMessageDetails(),
                sendMessageRequest.getReceiverType(), sendMessageRequest.getReceiverUniqueIdentifier(),
                sendMessageRequest.getReceiverPhone(), sendMessageRequest.getReceiverEmail(),
                sendMessageRequest.getServiceOID(), sendMessageRequest.getOperatorEGN());
        sendMessageResponse.setSendMessageResult(result);
        return sendMessageResponse;
    }

    public SendMessageOnBehalfOfResponse sendMessageOnBehalfOf(SendMessageOnBehalfOfRequest sendMessageOnBehalfOfRequest) {
        SendMessageOnBehalfOfResponse sendMessageOnBehalfOfResponse = new SendMessageOnBehalfOfResponse();
        Integer result = eDeliveryClient.geteDelivery().sendMessageOnBehalfOf(
                sendMessageOnBehalfOfRequest.getMessageDetails(),
                sendMessageOnBehalfOfRequest.getSenderType(),
                sendMessageOnBehalfOfRequest.getSenderUniqueIdentifier(),
                sendMessageOnBehalfOfRequest.getSenderPhone(),
                sendMessageOnBehalfOfRequest.getSenderEmail(),
                sendMessageOnBehalfOfRequest.getSenderFirstName(),
                sendMessageOnBehalfOfRequest.getSenderLastName(),
                sendMessageOnBehalfOfRequest.getReceiverType(),
                sendMessageOnBehalfOfRequest.getReceiverUniqueIdentifier(),
                sendMessageOnBehalfOfRequest.getServiceOID(),
                sendMessageOnBehalfOfRequest.getOperatorEGN());
        sendMessageOnBehalfOfResponse.setSendMessageOnBehalfOfResult(result);
        return sendMessageOnBehalfOfResponse;
    }

    public SendElectronicDocumentOnBehalfOfResponse sendElectronicDocumentOnBehalfOf(SendElectronicDocumentOnBehalfOfRequest sendElectronicDocumentOnBehalfOfRequest) {
        SendElectronicDocumentOnBehalfOfResponse sendElectronicDocumentOnBehalfOfResponse = new SendElectronicDocumentOnBehalfOfResponse();
        Integer result = eDeliveryClient.geteDelivery().sendElectronicDocumentOnBehalfOf(
                sendElectronicDocumentOnBehalfOfRequest.getSubject(),
                sendElectronicDocumentOnBehalfOfRequest.getDocBytes(),
                sendElectronicDocumentOnBehalfOfRequest.getDocNameWithExtension(),
                sendElectronicDocumentOnBehalfOfRequest.getDocRegNumber(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderType(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderUniqueIdentifier(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderPhone(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderEmail(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderFirstName(),
                sendElectronicDocumentOnBehalfOfRequest.getSenderLastName(),
                sendElectronicDocumentOnBehalfOfRequest.getReceiverType(),
                sendElectronicDocumentOnBehalfOfRequest.getReceiverUniqueIdentifier(),
                sendElectronicDocumentOnBehalfOfRequest.getServiceOID(),
                sendElectronicDocumentOnBehalfOfRequest.getOperatorEGN());
        sendElectronicDocumentOnBehalfOfResponse.setSendElectronicDocumentOnBehalfOfResult(result);
        return sendElectronicDocumentOnBehalfOfResponse;
    }

    public GetReceivedMessagesListPagedResponse getReceivedMessagesListPagedResponse(boolean onlyNew, int page, int size, String operatorEgn) {
        GetReceivedMessagesListPagedResponse response = new GetReceivedMessagesListPagedResponse();
        var result = eDeliveryClient.geteDelivery()
                .getReceivedMessagesListPaged(
                        onlyNew,
                        page,
                        size,
                        operatorEgn
                );
        response.setGetReceivedMessagesListPagedResult(result);
        return response;
    }

    public DcMessageDetails getReceivedMessageContentResponse(String messageId, String operatorEGN) {
        return eDeliveryClient.geteDelivery()
                .getReceivedMessageContent(Integer.parseInt(messageId), operatorEGN);
    }

    public SendMessageInReplyToResponse sendMessageInReplyTo(SendMessageInReplyToRequest sendMessageInReplyToRequest) {
        SendMessageInReplyToResponse sendMessageInReplyToResponse = new SendMessageInReplyToResponse();
        Integer result = eDeliveryClient.geteDelivery().sendMessageInReplyTo(
                sendMessageInReplyToRequest.getMessage(),
                sendMessageInReplyToRequest.getReplyToMessageId(),
                sendMessageInReplyToRequest.getServiceOID(),
                sendMessageInReplyToRequest.getOperatorEGN());
        sendMessageInReplyToResponse.setSendMessageInReplyToResult(result);
        return sendMessageInReplyToResponse;
    }

    public SendMessageOnBehalfToLegalEntityResponse sendMessageOnBehalfToLegalEntity(SendMessageOnBehalfToLegalEntityRequest sendMessageOnBehalfToLegalEntityRequest) {
        SendMessageOnBehalfToLegalEntityResponse sendMessageOnBehalfToLegalEntityResponse = new SendMessageOnBehalfToLegalEntityResponse();
        Integer result = eDeliveryClient.geteDelivery().sendMessageOnBehalfToLegalEntity(
                sendMessageOnBehalfToLegalEntityRequest.getMessage(),
                sendMessageOnBehalfToLegalEntityRequest.getSenderUniqueIdentifier(),
                sendMessageOnBehalfToLegalEntityRequest.getReceiverUniqueIdentifier(),
                sendMessageOnBehalfToLegalEntityRequest.getServiceOID(),
                sendMessageOnBehalfToLegalEntityRequest.getOperatorEGN());
        sendMessageOnBehalfToLegalEntityResponse.setSendMessageOnBehalfToLegalEntityResult(result);
        return sendMessageOnBehalfToLegalEntityResponse;
    }

    public SendMessageOnBehalfToPersonResponse sendMessageOnBehalfToPerson(SendMessageOnBehalfToPersonRequest sendMessageOnBehalfToPersonRequest) {
        SendMessageOnBehalfToPersonResponse sendMessageOnBehalfToPersonResponse = new SendMessageOnBehalfToPersonResponse();
        Integer result = eDeliveryClient.geteDelivery().sendMessageOnBehalfToPerson(
                sendMessageOnBehalfToPersonRequest.getMessage(),
                sendMessageOnBehalfToPersonRequest.getSenderUniqueIdentifier(),
                sendMessageOnBehalfToPersonRequest.getReceiverUniqueIdentifier(),
                sendMessageOnBehalfToPersonRequest.getReceiverPhone(),
                sendMessageOnBehalfToPersonRequest.getReceiverEmail(),
                sendMessageOnBehalfToPersonRequest.getReceiverFirstName(),
                sendMessageOnBehalfToPersonRequest.getReceiverLastName(),
                sendMessageOnBehalfToPersonRequest.getServiceOID(),
                sendMessageOnBehalfToPersonRequest.getOperatorEGN());
        sendMessageOnBehalfToPersonResponse.setSendMessageOnBehalfToPersonResult(result);
        return sendMessageOnBehalfToPersonResponse;
    }

}


