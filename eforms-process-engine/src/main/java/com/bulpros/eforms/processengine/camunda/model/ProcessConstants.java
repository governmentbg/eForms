package com.bulpros.eforms.processengine.camunda.model;

public interface ProcessConstants {
    String GROUP = "group";
    String SHOW_LANE = "show";
    String IS_COMPLETABLE = "isCompletable";
    String IS_FINAL = "isFinal";
    String GO_TO_NEXT_STEP = "goToNextStep";
    String INITIATOR = "initiator";
    String CONTEXT = "context";

    String EMBEDDED_SUFFIX = "_embedded";
    String SUBMISSION_DATA = "submissionData_";
    String SUBMISSION_ID = "submissionId_";
    String FORMA_REQUEST_SUFFIX = "_forma_request";
    String FORMA_REQUEST_EMBEDDED_SUFFIX = "forma_request_embedded";
    String FORMA_RESPONSE_SUFFIX = "_forma_response";
    String EDELIVERY_FILES_PACKAGE = "eDeliveryFilesPackage_";
    String EDELIVERY_FILES_PACKAGE_STATUS = "eDeliveryFilesPackageStatus";
    String EDELIVERY_SIGNED_FILES_PACKAGE = "eDeliverySignedFilesPackage_";
    String EDELIVERY_ATTACHMENTS = "eDeliveryAttachments_";
    String PROCESS_HAS_SIGNING = "hasSigning";

    String SUB = "sub";
    String PREFERRED_USERNAME = "preferred_username";
    String PERSON_IDENTIFIER = "personIdentifier";
    String TASK_INSTANCE_INDEX = "loopCounter";
    String PRINCIPAL_ASSURANCE_LEVEL = "assurance_level";
}
