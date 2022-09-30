package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.enums.UserStageTypeEnum;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import com.bulpros.eforms.processengine.camunda.repository.CamundaTaskRepository;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.exeptions.ForbiddenTaskException;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.dto.enums.UserTaskStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service(value = "camundaLaneService")
public class CamundaLaneService implements LaneService {

    private final CamundaProcessRepository camundaProcessRepository;
    private final CamundaTaskRepository camundaTaskRepository;
    private final ExpressionEvaluationService expressionEvaluationService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public ArrayList<com.bulpros.eforms.processengine.camunda.model.Lane> getLanesByStageForUser(String processInstanceId,
                                                                                                 UserStageTypeEnum userStageTypeEnum) {
        return getLanesByStage(processInstanceId, userStageTypeEnum, true);
    }

    @Override
    public ArrayList<com.bulpros.eforms.processengine.camunda.model.Lane> getAllLanesByStage(String processInstanceId,
                                                                                             UserStageTypeEnum userStageTypeEnum) {
        return getLanesByStage(processInstanceId, userStageTypeEnum, false);
    }

    private ArrayList<com.bulpros.eforms.processengine.camunda.model.Lane> getLanesByStage(String processInstanceId,
                                                                                           UserStageTypeEnum userStageTypeEnum,
                                                                                           boolean checkAssigneeAndUserGroup) {
        Collection<org.camunda.bpm.model.bpmn.instance.Lane> camundaLanes = this.camundaProcessRepository.
                getProcessLanes(processInstanceId);

        camundaLanes = camundaLanes.stream().filter(lane -> lane.getChildLaneSet() == null)
                .filter(lane -> userStageTypeEnum.stageType
                        .equalsIgnoreCase(this.camundaProcessRepository.getCamundaPropertyStaticValueByName(lane, ProcessConstants.GROUP)))
                .filter(lane -> {
                    if (StringUtils.isEmpty(this.camundaProcessRepository.getCamundaPropertyStaticValueByName(lane, ProcessConstants.SHOW_LANE)))
                        return true;
                    return expressionEvaluationService.evaluateBoolean(this.camundaProcessRepository
                            .getCamundaPropertyStaticValueByName(lane, ProcessConstants.SHOW_LANE), processInstanceId);
                })
                .collect(Collectors.toList());


        List<UserTask> taskFlow = this.camundaTaskRepository.getDefaultUserTasksFlow(processInstanceId, userStageTypeEnum);

        ArrayList<com.bulpros.eforms.processengine.camunda.model.Lane> eformsLanes = new ArrayList<>();
        for (org.camunda.bpm.model.bpmn.instance.Lane camundaLane : camundaLanes) {
            com.bulpros.eforms.processengine.camunda.model.Lane eformsLane = this.convertLaneToModel(camundaLane,
                    processInstanceId, taskFlow, userStageTypeEnum, checkAssigneeAndUserGroup);
            eformsLanes.add(eformsLane);
        }
        return eformsLanes;
    }

    public com.bulpros.eforms.processengine.camunda.model.Lane convertLaneToModel(org.camunda.bpm.model.bpmn.instance.Lane lane,
                                                                                  String processInstanceId, List<UserTask> defaultUserTasksFlow,
                                                                                  UserStageTypeEnum userStageTypeEnum,
                                                                                  boolean checkAssigneeAndUserGroup) {
        com.bulpros.eforms.processengine.camunda.model.Lane eFormsLane = this.modelMapper.map(lane,
                com.bulpros.eforms.processengine.camunda.model.Lane.class);

        Collection<CamundaProperty> camundaProperties = this.camundaProcessRepository.getCamundaStaticProperties(lane);
        if (camundaProperties != null) {
            HashMap<String, String> laneProperties = (HashMap<String, String>) camundaProperties.stream()
                    .filter(p -> !p.getCamundaName().equals(ProcessConstants.SHOW_LANE))
                    .collect(Collectors.toMap(CamundaProperty::getCamundaName, CamundaProperty::getCamundaValue));
            eFormsLane.setProperties(laneProperties);
        }
        retrieveCompletedTasks(eFormsLane, lane, processInstanceId, userStageTypeEnum, checkAssigneeAndUserGroup);

        retrieveCurrentAndNextTasks(eFormsLane, lane, defaultUserTasksFlow, processInstanceId, userStageTypeEnum, checkAssigneeAndUserGroup);

        return eFormsLane;
    }

    private void normalizeFormKey(com.bulpros.eforms.processengine.camunda.model.UserTask userTask, String processInstanceId) {
        String formApiPath = Arrays.stream(EFormsUtils.getFormApiPath(userTask.getFormKey()).trim().split("[ /]+"))
                .map(p -> {
                    if (expressionEvaluationService.isElExpression(p))
                        return expressionEvaluationService.evaluateString(p, processInstanceId);
                    return p;
                })
                .collect(Collectors.joining("/"));

        Map<String, String> queryParamsPairs = getFormKeyQueryParams(userTask);
        for (Map.Entry<String, String> queryPair : queryParamsPairs.entrySet()) {
            if (expressionEvaluationService.isElExpression(queryPair.getValue())) {
                String value = expressionEvaluationService.evaluateString(queryPair.getValue(), processInstanceId);
                queryPair.setValue(value);
            }
        }

        userTask.setFormKey(constructFormKey(formApiPath, queryParamsPairs));

    }

    private Map<String, String> getFormKeyQueryParams(com.bulpros.eforms.processengine.camunda.model.UserTask userTask) {
        Map<String, String> queryPairs = new HashMap<>();
        String[] formKeySplit = userTask.getFormKey().split("\\?");
        if (formKeySplit.length > 1) {
            String[] pairs = formKeySplit[1].split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                queryPairs.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }
        return queryPairs;
    }

    private String constructFormKey(String formApiPath, Map<String, String> queryParamsPairs) {
        String queryPart = "";
        for (Map.Entry<String, String> queryParamPair : queryParamsPairs.entrySet()) {
            queryPart = queryPart.concat(queryParamPair.getKey() + "=" + queryParamPair.getValue());
            if (!StringUtils.isEmpty(queryPart)) {
                queryPart = queryPart.concat("&");
            }
        }
        if (queryPart.endsWith("&")) {
            queryPart = queryPart.substring(0, queryPart.length() - 1);
        }
        if (!StringUtils.isEmpty(queryPart)) {
            return formApiPath + "?" + queryPart;
        }
        return formApiPath;
    }

    private void retrieveCompletedTasks(com.bulpros.eforms.processengine.camunda.model.Lane eFormsLane,
                                        org.camunda.bpm.model.bpmn.instance.Lane lane, String processInstanceId,
                                        UserStageTypeEnum userStageTypeEnum,
                                        boolean checkAssigneeAndUserGroup) {
        List<UserTask> laneUserTasks = new ArrayList<>(this.camundaTaskRepository.getAllTasksForLane(lane, processInstanceId,
                userStageTypeEnum, checkAssigneeAndUserGroup));
        List<HistoricTaskInstance> finishedTasks = this.camundaTaskRepository.getFinishedTasksForProcessInstance(processInstanceId);

        Optional<String> optionalUser = Optional.ofNullable(userService.getPrincipalIdentifier());
        String user = optionalUser
                .orElseThrow(() -> new ForbiddenTaskException("User identifier is not present"));

        finishedTasks = finishedTasks.stream().filter(task -> task.getAssignee().equals(user)).collect(Collectors.toList());

        int order = 0;
        for (HistoricTaskInstance historyUserTask : finishedTasks) {
            Optional<UserTask> userTask = laneUserTasks.stream().
                    filter(task -> task.getId().equals(historyUserTask.getActivityInstanceId().split(":")[0])).findFirst();
            if (userTask.isPresent()) {
                com.bulpros.eforms.processengine.camunda.model.UserTask eFormsUserTask =
                        modelMapper.map(userTask.get(), com.bulpros.eforms.processengine.camunda.model.UserTask.class);
                if (!StringUtils.isEmpty(eFormsUserTask.getFormKey())) {
                    normalizeFormKey(eFormsUserTask, processInstanceId);
                }
                HashMap<String, String> taskProperties = this.camundaTaskRepository.getTaskProperties(userTask.get(), processInstanceId);
                if (taskProperties != null) {
                    eFormsUserTask.setProperties(taskProperties);
                }
                eFormsUserTask.getProperties().put("order", String.valueOf(order++));
                eFormsUserTask.setStatus(UserTaskStatus.COMPLETED);
                eFormsLane.addUserTask(eFormsUserTask);
            }
        }

        Integer duplicateHistoryUserTaskIndex = null;
        for (int i = 0; i < eFormsLane.getUserTasks().size(); i++) {
            List<Task> activeTasks = this.camundaTaskRepository.getActiveTasksForProcessInstance(processInstanceId);
            com.bulpros.eforms.processengine.camunda.model.UserTask currentLoopUserTask = eFormsLane.getUserTasks().get(i);
            Optional<Task> activeTask = activeTasks.stream()
                    .filter(task -> task.getTaskDefinitionKey().equals(currentLoopUserTask.getId())).findFirst();
            if (activeTask.isPresent()) {
                duplicateHistoryUserTaskIndex = i;
                break;
            }
        }
        if (duplicateHistoryUserTaskIndex != null) {
            eFormsLane.getUserTasks().subList(duplicateHistoryUserTaskIndex, eFormsLane.getUserTasks().size()).clear();
        }

        HashSet<Object> addedHistoryUserTask = new HashSet<>();
        eFormsLane.getUserTasks().removeIf(e -> !addedHistoryUserTask.add(e.getId()));
    }

    private void retrieveCurrentAndNextTasks(com.bulpros.eforms.processengine.camunda.model.Lane eFormsLane, org.camunda.bpm.model.bpmn.instance.Lane lane,
                                             List<UserTask> defaultUserTasksFlow, String processInstanceId,
                                             UserStageTypeEnum userStageTypeEnum,
                                             boolean checkAssigneeAndUserGroup) {
        Collection<UserTask> laneUserTasks = this.camundaTaskRepository.getAllTasksForLane(lane, processInstanceId, userStageTypeEnum, checkAssigneeAndUserGroup);
        List<UserTask> camundaUserTasks = defaultUserTasksFlow.stream().filter(laneUserTasks::contains).collect(Collectors.toList());
        List<Task> activeTasks = this.camundaTaskRepository.getActiveTasksForProcessInstance(processInstanceId);
        int order = eFormsLane.getUserTasks().size();
        for (UserTask userTask : camundaUserTasks) {
            com.bulpros.eforms.processengine.camunda.model.UserTask eFormsUserTask =
                    modelMapper.map(userTask, com.bulpros.eforms.processengine.camunda.model.UserTask.class);

            if (!StringUtils.isEmpty(eFormsUserTask.getFormKey())) {
                normalizeFormKey(eFormsUserTask, processInstanceId);
            }

            HashMap<String, String> taskProperties = this.camundaTaskRepository.getTaskProperties(userTask, processInstanceId);
            if (taskProperties != null) {
                eFormsUserTask.setProperties(taskProperties);
            }
            eFormsUserTask.getProperties().put("order", String.valueOf(order++));

            Optional<Task> activeTask = activeTasks.stream()
                    .filter(task -> task.getName().equals(eFormsUserTask.getName())).findFirst();
            if (activeTask.isPresent()) {
                eFormsUserTask.setStatus(UserTaskStatus.STARTED);
                eFormsUserTask.setAssignee(activeTask.get().getAssignee());
                eFormsUserTask.setId(activeTask.get().getId());
                eFormsUserTask.setDefinitionKey(activeTask.get().getTaskDefinitionKey());

            }

            eFormsLane.addUserTask(eFormsUserTask);
        }
    }

}
