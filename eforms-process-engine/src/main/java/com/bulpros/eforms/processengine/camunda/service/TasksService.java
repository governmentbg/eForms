package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.web.dto.ProcessDto;

import java.util.Map;

public interface TasksService {

    ProcessDto getCurrentUserTaskByProcessId(String processId);

    void completeTask(String taskId, Map<String, Object> variables);
}
