package com.bulpros.eformsgateway.process.repository;

import java.util.List;

import com.bulpros.eformsgateway.process.repository.dto.HistoryTaskDto;
import com.bulpros.eformsgateway.process.repository.dto.TaskDto;

public interface TaskRepository {
    
    List<TaskDto> getAllTasksByAssignee(String authorizationToken, String personIdentifier);
    
    List<TaskDto> getAllTasksByAssignee(String authorizationToken, String personIdentifier, String cacheControl);

    TaskDto getTaskById(String authorizationToken, String taskId);
    
    TaskDto getTaskById(String authorizationToken, String taskId, String cacheControl);

    List<TaskDto> getAllTasksByProcessInstanceId(String authorizationToken, String processInstanceId);
    
    List<TaskDto> getAllTasksByProcessInstanceId(String authorizationToken, String processInstanceId, String cacheControl);

    List<HistoryTaskDto> getAllHistoryTasksByProcessInstanceId(String authorizationToken, String processInstanceId);
    
    List<HistoryTaskDto> getAllHistoryTasksByProcessInstanceId(String authorizationToken, String processInstanceId, String cacheControl);

    List<TaskDto> getActiveTasksByProcessInstanceBusinessKeyAndAssignee(String authorizationToken, String processInstanceBusinessKey, String assignee);
}
