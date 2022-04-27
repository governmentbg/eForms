package com.bulpros.eformsgateway.process.web.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryTaskResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String processDefinitionKey;
    private String processDefinitionId;
    private String processInstanceId;
    private String executionId;
    private String caseDefinitionKey;
    private String caseDefinitionId;
    private String caseInstanceId;
    private String caseExecutionId;
    private String activityInstanceId;
    private String name;
    private String description;
    private String deleteReason;
    private String owner;
    private String assignee;
    private String assigneeFullName;
    private String startTime;
    private String endTime;
    private Long duration;
    private String taskDefinitionKey;
    private Integer priority;
    private String due;
    private String parentTaskId;
    private String followUp;
    private String tenantId;
    private String removalTime;
    private String rootProcessInstanceId;
}
