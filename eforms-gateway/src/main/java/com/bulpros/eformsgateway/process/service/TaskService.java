package com.bulpros.eformsgateway.process.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.bulpros.eformsgateway.process.web.dto.HistoryTaskResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;

public interface TaskService {
    List<String> getProcessInstanceIdsForAllTasksByAssignee(Authentication authentication);
    
    TaskResponseDto getTaskById(Authentication authentication, String taskId);

    List<TaskResponseDto> getAllTasksByProcessInstanceId(Authentication authentication, String processInstanceId);

    List<HistoryTaskResponseDto> getAllHistoryTasksByProcessInstanceId(Authentication authentication, String processInstanceId);

    List<TaskResponseDto> getActiveTasksByProcessInstanceBusinessKeyAndAssignee(Authentication authentication, String processInstanceBusinessKey, String assignee);
}
