package com.bulpros.eforms.processengine.camunda.model;

public interface ProcessConstants {
    String GROUP = "group";
    String INITIATOR = "initiator";
    String CONTEXT = "context";

    String EMBEDDED_SUFFIX = "_embedded";
    String SUBMISSION_DATA = "submissionData_";
    String SUBMISSION_ID = "submissionId_";
    String EDELIVERY_FILES_PACKAGE = "eDeliveryFilesPackage_";
    String EDELIVERY_SIGNED_FILES_PACKAGE = "eDeliverySignedFilesPackage_";
    String EDELIVERY_ATTACHMENTS = "eDeliveryAttachments_";
    String PROCESS_HAS_SIGNING = "hasSigning";

    String SUB = "sub";
    String PREFERRED_USERNAME = "preferred_username";
    String PERSON_IDENTIFIER = "personIdentifier";
    String TASK_INSTANCE_INDEX = "loopCounter";
    String PRINCIPAL_ASSURANCE_LEVEL = "assurance_level";
}
