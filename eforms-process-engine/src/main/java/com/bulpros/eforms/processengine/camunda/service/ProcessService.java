package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.Process;
import com.bulpros.eforms.processengine.camunda.model.enums.UserStageTypeEnum;

import java.util.Map;

public interface ProcessService {
    Process getProcess(String processInstanceId, UserStageTypeEnum userStageTypeEnum);

    Process getProcessWithNoDefaultUser(String processInstanceId, UserStageTypeEnum userStageTypeEnum);

    Map<String, Object> getProcessVariables(String processInstanceId);
}
