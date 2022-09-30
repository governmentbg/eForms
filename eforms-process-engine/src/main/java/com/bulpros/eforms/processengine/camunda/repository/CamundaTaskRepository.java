package com.bulpros.eforms.processengine.camunda.repository;

import com.bulpros.eforms.processengine.camunda.model.enums.UserStageTypeEnum;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.UserTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CamundaTaskRepository {

    void completeTaskByTaskId(String taskId, Map<String, Object> variables);
    boolean isTaskCompletable(String processInstanceId, String taskId);
    Task getTaskByTaskId(String taskId);
    Map<String, Object> getTaskVariablesByTaskId(String taskId);
    List<Task> getActiveTasksForProcessInstance(String processInstanceId);
    List<HistoricTaskInstance> getFinishedTasksForProcessInstance(String processInstanceId);
    HistoricTaskInstance getLastFinishedTaskForProcessInstance(String processInstanceId);
    Collection<UserTask> getAllTasksForLane(Lane lane, String processInstanceId, UserStageTypeEnum userStageTypeEnum, boolean checkAssigneeAndUserGroup);
    HashMap<String, String> getTaskProperties(UserTask userTask, String processInstanceId);
    List<UserTask> getDefaultUserTasksFlow(String processInstanceId, UserStageTypeEnum userStageTypeEnum);
}
