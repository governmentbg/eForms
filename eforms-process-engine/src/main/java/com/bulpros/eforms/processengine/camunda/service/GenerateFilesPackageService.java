package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.enums.GenerateFilesParticipantType;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface GenerateFilesPackageService {

    void generate(Authentication authentication, String projectId, String arId,
                  String businessKey, Map<String, Object> formSubmissionsSubjectToFilesGeneration,
                  GenerateFilesParticipantType participantType, DelegateExecution delegateExecution);

}
