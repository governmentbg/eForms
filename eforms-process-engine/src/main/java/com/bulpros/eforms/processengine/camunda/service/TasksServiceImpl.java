package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.Lane;
import com.bulpros.eforms.processengine.camunda.model.Process;
import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.UserTask;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.repository.CamundaTaskRepository;
import com.bulpros.eforms.processengine.exeptions.ForbiddenTaskException;
import com.bulpros.eforms.processengine.exeptions.ProcessNotFoundException;
import com.bulpros.eforms.processengine.exeptions.TaskNotFoundException;
import com.bulpros.eforms.processengine.exeptions.VariableNotFoundException;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.dto.ProcessDto;
import com.bulpros.eforms.processengine.web.dto.enums.UserTaskStatus;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.task.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final CamundaProcessRepository processRepository;
    private final CamundaTaskRepository taskRepository;
    private final ProcessService processService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public ProcessDto getCurrentUserTaskByProcessId(String processId) {
        Optional.ofNullable(processRepository.getActivityInstanceByProcessById(processId))
            .orElseThrow(() ->
                   new ProcessNotFoundException(MessageFormat.format("Process {0} does not exists", processId)));

        Optional<String> optionalUser = Optional.ofNullable(userService.getPrincipalIdentifier());
        String user = optionalUser
                .orElseThrow(() -> new ForbiddenTaskException("User identifier is not present"));

        Process process = processService.getProcessWithNoDefaultUser(processId, UserStageTypeEnum.REQUESTOR);
        process.setLanes(getCurrentTaskLane(process.getLanes(), user));

        return modelMapper.map(process, ProcessDto.class);
    }

    private ArrayList<Lane> getCurrentTaskLane(List<Lane> lanes, String currentUser) {
        Lane lane = null;
        for (Lane curLane : lanes) {
            for (UserTask userTask : curLane.getUserTasks()) {
                if (userTask.getStatus().equals(UserTaskStatus.STARTED) && userTask.getAssignee().equals(currentUser)) {
                    lane = curLane;
                    ArrayList<UserTask> userTaskArrayList = new ArrayList<>(Collections.singletonList(userTask));
                    lane.setUserTasks(userTaskArrayList);
                    return new ArrayList<>(Collections.singletonList(lane));
                }
            }
        }

       throw new ForbiddenTaskException("User is not assigned to the task");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void completeTask(String taskId, Map<String, Object> variables) {
        Task curTask = Optional.ofNullable(taskRepository.getTaskByTaskId(taskId))
                .orElseThrow(() -> new TaskNotFoundException(MessageFormat.format("Task {0} does not exist", taskId)));
        if(!taskRepository.isTaskCompletable(curTask.getProcessInstanceId(), curTask.getTaskDefinitionKey())) {
            throw new ForbiddenTaskException("Task is not completable");
        }
        Map<String, Object> submissionDataKeyMap = Optional.ofNullable(variables.get("variables"))
                .map(obj -> (Map<String, Object>) obj)
                .orElseThrow(() -> new VariableNotFoundException("Variables not present"));
        String formDataSubmissionKey = submissionDataKeyMap.keySet().stream().findFirst().get();
        Map<String, Object> valuesMap = submissionDataKeyMap.values().stream().map(e -> (Map<String, Object>) e).findFirst().get();
        Map<String, Object> dataKeyMap = valuesMap.values().stream().map(e -> (Map<String, Object>) e).findFirst().get();
        Map<String, Object> usersSubmissionData = addSubmissionsDataForCurrentTask(curTask.getId(), formDataSubmissionKey, dataKeyMap);
        this.taskRepository.completeTaskByTaskId(taskId, usersSubmissionData);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> addSubmissionsDataForCurrentTask(String taskId, String key, Map<String, Object> submissionData) {
        Map<String, Object> taskVariables = taskRepository.getTaskVariablesByTaskId(taskId);
        boolean isMultiTask = taskVariables.containsKey(ProcessConstants.TASK_INSTANCE_INDEX);
        Map<String, Object> previousSubmissionData = Optional.ofNullable(taskVariables.get(key))
                .map(obj -> (Map<String, Object>) obj)
                .orElse(new HashMap<>());
        previousSubmissionData.put(userService.getPrincipalIdentifier(), submissionData);
        return Collections.singletonMap(key, isMultiTask ? previousSubmissionData : submissionData);
    }
}
