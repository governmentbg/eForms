package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.camunda.service.TasksService;
import com.bulpros.eforms.processengine.exeptions.ProcessNotFoundException;
import com.bulpros.eforms.processengine.exeptions.TaskNotFoundException;
import com.bulpros.eforms.processengine.web.dto.ProcessDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/eforms-rest"})
@RequiredArgsConstructor
public class TaskController {

    private final TasksService tasksService;

    @Timed(value = "eforms-process-engine-current_task.time")
    @GetMapping(value = "process/{processId}/current-task", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessDto> getAssignedTask(@PathVariable("processId") String processId) {
        if (Strings.isEmpty(processId)) {
            throw new ProcessNotFoundException("process ID is not present");
        }

        ProcessDto processDto = this.tasksService.getCurrentUserTaskByProcessId(processId);
        return ResponseEntity.ok(processDto);
    }


    @Timed(value = "eforms-process-engine-task_complete.time")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "task/{taskId}/complete")
    public void completeTaskById(@PathVariable("taskId") String taskId, @RequestBody Map<String, Object> variables) {
        if (Strings.isEmpty(taskId)) {
            throw new TaskNotFoundException("task ID is not present");
        }

        this.tasksService.completeTask(taskId, variables);
    }
}
