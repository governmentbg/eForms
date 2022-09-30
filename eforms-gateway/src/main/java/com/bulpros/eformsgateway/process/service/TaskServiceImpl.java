package com.bulpros.eformsgateway.process.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.process.repository.TaskRepository;
import com.bulpros.eformsgateway.process.repository.dto.HistoryTaskDto;
import com.bulpros.eformsgateway.process.repository.dto.TaskDto;
import com.bulpros.eformsgateway.process.web.dto.HistoryTaskResponseDto;
import com.bulpros.eformsgateway.process.web.dto.TaskResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    @Value("${com.bulpros.formio.userprofile.project.id}")
    private String userProfileProjectId;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;
    private final UserProfileService userProfileService;
    private final UserService userService;

    @Override
    public TaskResponseDto getTaskById(Authentication authentication, String taskId) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        return modelMapper.map(
                taskRepository.getTaskById(token, taskId), 
                TaskResponseDto.class);
    }

    @Override
    public List<String> getProcessInstanceIdsForAllTasksByAssignee(Authentication authentication) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        List<TaskDto> taskDtos = taskRepository.getAllTasksByAssignee(token, user.getPersonIdentifier());
        List<String> processInstanceIds = new ArrayList<>();
        if (taskDtos == null || taskDtos.isEmpty()) {
            log.debug("User: {} has no tasks assigned." + user.getPersonIdentifier());
        } else {
            processInstanceIds = taskDtos.stream().map(t -> t.getProcessInstanceId()).collect(Collectors.toList());
        }
        return processInstanceIds;
    }

    @Override
    public List<TaskResponseDto> getAllTasksByProcessInstanceId(Authentication authentication, String processInstanceId) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        List<TaskDto> taskDtos = taskRepository.getAllTasksByProcessInstanceId(token, processInstanceId);
        if (taskDtos == null || taskDtos.isEmpty()) {
            log.debug("No active tasks for processInstanceId: {} are found." + processInstanceId);
            return Arrays.asList();
        }
        List<TaskResponseDto> taskResponseDtos = taskDtos.stream().map(t -> modelMapper.map(t, TaskResponseDto.class)).collect(Collectors.toList());
        List<String> userIds = taskResponseDtos.stream().map(t -> t.getAssignee()).collect(Collectors.toList());
        Map<String, String> userIdToUserNameMap = userProfileService.getUserIdToUserNameMap(userProfileProjectId, authentication, userIds);
        if (userIdToUserNameMap != null && !userIdToUserNameMap.isEmpty()) {
            taskResponseDtos.forEach(t -> t.setAssigneeFullName(userIdToUserNameMap.get(t.getAssignee())));
        }
        return taskResponseDtos;
    }

    @Override
    public List<HistoryTaskResponseDto> getAdminHistoryTasksByProcessInstanceId(Authentication authentication, String processInstanceId) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        List<HistoryTaskDto> historyTaskDtos = taskRepository.getAllHistoryTasksByProcessInstanceId(token, processInstanceId);
        if (historyTaskDtos == null || historyTaskDtos.isEmpty()) {
            log.debug("No history tasks for processInstanceId: {} are found." + processInstanceId);
            return Arrays.asList();
        }
        historyTaskDtos = historyTaskDtos.stream()
                .filter(task -> task.getName().contains("PROCESS.ADMINTASK")).collect(Collectors.toList());
        List<HistoryTaskResponseDto> historyTaskResponseDtos = historyTaskDtos.stream()
                .map(t -> modelMapper.map(t, HistoryTaskResponseDto.class)).collect(Collectors.toList());
        List<String> userIds = historyTaskResponseDtos.stream().map(t -> t.getAssignee()).collect(Collectors.toList());
        Map<String, String> userIdToUserNameMap = userProfileService.getUserIdToUserNameMap(userProfileProjectId, authentication, userIds);
        if (userIdToUserNameMap != null && !userIdToUserNameMap.isEmpty()) {
            historyTaskResponseDtos.forEach(t -> t.setAssigneeFullName(userIdToUserNameMap.get(t.getAssignee())));
        }
        return historyTaskResponseDtos;
    }

    @Override
    public List<TaskResponseDto> getActiveTasksByProcessInstanceBusinessKeyAndAssignee(Authentication authentication, String processInstanceBusinessKey, String assignee) {
        var user = userService.getUser(authentication);
        var token = user.getToken();
        List<TaskDto> tasks = taskRepository.getActiveTasksByProcessInstanceBusinessKeyAndAssignee(token, processInstanceBusinessKey, assignee);
        if (tasks == null || tasks.isEmpty()) {
            return Arrays.asList();
        }
        return tasks.stream().map(t -> modelMapper.map(t, TaskResponseDto.class)).collect(Collectors.toList());
    }
}
