package com.bulpros.eforms.processengine.camunda.repository;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.service.UserStageTypeEnum;
import com.bulpros.eforms.processengine.security.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Query;
import org.camunda.bpm.model.bpmn.impl.instance.ExclusiveGatewayImpl;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component(value = "camundaTaskRepository")
@RequiredArgsConstructor
public class CamundaTaskRepositoryImpl implements CamundaTaskRepository {

    private final TaskService taskService;
    private final HistoryService historyService;
    private final CamundaProcessRepository camundaProcessRepository;
    private final UserService userService;

    @Override
    public void completeTaskByTaskId(String taskId, Map<String, Object> variables) {
        this.taskService.complete(taskId, variables);
    }

    @Override
    public Task getTaskByTaskId(String taskId) {
        return this.taskService
                .createTaskQuery().taskId(taskId).singleResult();
    }

    @Override
    public Map<String, Object> getTaskVariablesByTaskId(String taskId) {
        return this.taskService.getVariables(taskId);
    }

    @Override
    public List<Task> getActiveTasksForProcessInstance(String processInstanceId) {
        return this.taskService
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .list();
    }

    @Override
    public List<UserTask> getDefaultUserTasksFlow(String processInstanceId, UserStageTypeEnum userStageTypeEnum) {
        List<Task> activeTasks = this.taskService
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .active().or()
                .taskAssignee(userService.getPrincipalIdentifier())
                .taskCandidateGroup(userStageTypeEnum.stageType).endOr()
                .orderByTaskPriority().asc()
                .list();
        return toDefaultFlow(activeTasks, processInstanceId);
    }


    @Override
    public List<HistoricTaskInstance> getFinishedTasksForProcessInstance(String processInstanceId) {
        return this.historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished()
                .orderByHistoricActivityInstanceStartTime().asc().list();
    }

    @Override
    public Collection<UserTask> getAllTasksForLane(Lane lane, String processInstanceId, UserStageTypeEnum userStageTypeEnum, boolean checkAssigneeAndUserGroup) {
        Set<String> flowNodes = lane.getFlowNodeRefs().stream().map(FlowElement::getName)
                .collect(Collectors.toSet());

        // Collect all tasks for current lane
        Collection<UserTask> camundaUserTasks = lane.getModelInstance().getModelElementsByType(UserTask.class).stream()
                .filter(userTask -> flowNodes.contains(userTask.getName())).collect(Collectors.toList());

        // Remove all without assignee and without candidate group
        camundaUserTasks = camundaUserTasks.stream()
                .filter(task -> task.getCamundaAssignee() != null || task.getCamundaCandidateGroupsList() != null)
                .collect(Collectors.toList());

        if (checkAssigneeAndUserGroup) {
            // check assignee with user preferred name

            camundaUserTasks = camundaUserTasks.stream()
                    .filter(task -> isCamundaAssigneeNotPresent(task) || isPrincipleProcessInitiator(processInstanceId))
                    .collect(Collectors.toList());

            camundaUserTasks = camundaUserTasks.stream()
                    .filter(userTask -> compareTaskGroupWithStageType(userTask, userStageTypeEnum))
                    .collect(Collectors.toList());
        }

        return camundaUserTasks;
    }

    @Override
    public HashMap<String, String> getTaskProperties(UserTask userTask) {
        ExtensionElements extensionElements = userTask.getExtensionElements();
        if (extensionElements == null) return null;
        Query<CamundaProperties> result = extensionElements.getElementsQuery()
                .filterByType(CamundaProperties.class);
        int resultCount = extensionElements.getElementsQuery().filterByType(CamundaProperties.class).count();
        if (resultCount == 0) return null;
        return (HashMap<String, String>) result.singleResult().getCamundaProperties().stream()
                .collect(Collectors.toMap(CamundaProperty::getCamundaName, CamundaProperty::getCamundaValue));
    }

    @Override
    public boolean isTaskCompletable(String processInstanceId, String taskId) {
        UserTask userTask = camundaProcessRepository.getUserTaskById(processInstanceId, taskId);
        Collection<CamundaProperty> userTaskProperties = camundaProcessRepository.getCamundaStaticProperties(userTask);
        if (userTaskProperties != null) {
            HashMap<String, String> properties = (HashMap<String, String>) userTaskProperties.stream()
                    .collect(Collectors.toMap(CamundaProperty::getCamundaName, CamundaProperty::getCamundaValue));
            if (properties.get("isCompletable") != null) {
                return Boolean.parseBoolean(properties.get("isCompletable"));
            }
        }
        return true;
    }

    private List<UserTask> toDefaultFlow(List<Task> activeTasks, String processInstanceId) {
        if (activeTasks.isEmpty()) {
            return new ArrayList<UserTask>();
        }
        Task currentTask = activeTasks.get(0);
        List<UserTask> defaultUserTaskFlow = new ArrayList<>();
        UserTask currentUserTask = camundaProcessRepository.getUserTaskById(processInstanceId, currentTask.getTaskDefinitionKey());
        defaultUserTaskFlow.add(currentUserTask);
        return findDefaultFlow(currentUserTask, defaultUserTaskFlow);
    }

    private boolean compareTaskGroupWithStageType(UserTask userTask, UserStageTypeEnum userStageTypeEnum) {
        String candidateGroups = userTask.getCamundaCandidateGroups();
        if (candidateGroups == null) {
            return true;
        }
        return candidateGroups.equalsIgnoreCase(userStageTypeEnum.stageType);
    }

    private List<UserTask> findDefaultFlow(FlowNode flowNode, List<UserTask> defaultUserTaskFlow) {
        if (flowNode.getOutgoing().isEmpty()) {
            Collection<BoundaryEvent> boundaryEvents = flowNode.getModelInstance().getModelElementsByType(BoundaryEvent.class);
            for (BoundaryEvent boundaryEvent : boundaryEvents) {
                if (boundaryEvent.getAttachedTo().getId().equals(flowNode.getId()) && !boundaryEvent.equals(flowNode)) {
                    flowNode = boundaryEvent;
                }
            }
        }
        while (!flowNode.getOutgoing().isEmpty()) {
            Collection<SequenceFlow> outgoingSequenceFlows = flowNode.getOutgoing();
            for (SequenceFlow sequenceFlow : outgoingSequenceFlows) {
                FlowNode nextFlowNode = sequenceFlow.getTarget();
                if (nextFlowNode instanceof UserTask) {
                    defaultUserTaskFlow.add((UserTask) nextFlowNode);
                    flowNode = nextFlowNode;
                } else if (nextFlowNode instanceof ExclusiveGateway) {
                    SequenceFlow defaultFlow = ((ExclusiveGatewayImpl) nextFlowNode).getDefault();
                    FlowNode defaultFlowNode = defaultFlow.getTarget();
                    if (defaultFlowNode instanceof UserTask) {
                        defaultUserTaskFlow.add((UserTask) defaultFlowNode);
                    }
                    flowNode = defaultFlowNode;
                } else if (nextFlowNode instanceof ComplexGateway) {
                    SequenceFlow defaultFlow = ((ComplexGateway) nextFlowNode).getDefault();
                    FlowNode defaultFlowNode = defaultFlow.getTarget();
                    if (defaultFlowNode instanceof UserTask) {
                        defaultUserTaskFlow.add((UserTask) defaultFlowNode);
                    }
                    flowNode = defaultFlowNode;
                } else if (nextFlowNode instanceof ParallelGateway) {
                    Collection<SequenceFlow> outgoingFlows = ((ParallelGateway) nextFlowNode).getOutgoing();
                    if (outgoingFlows.size() > 1) {
                        for (SequenceFlow outgoingFlow : outgoingFlows) {
                            ExtensionElements extensionElements = outgoingFlow.getExtensionElements();
                            if (extensionElements == null) continue;
                            Query<CamundaProperties> result = extensionElements.getElementsQuery()
                                    .filterByType(CamundaProperties.class);
                            int resultCount = extensionElements.getElementsQuery().filterByType(CamundaProperties.class).count();
                            if (resultCount == 0) continue;
                            if (result.singleResult().getCamundaProperties().stream()
                                    .anyMatch(e -> e.getCamundaName().equals("default"))) {
                                FlowNode defaultFlowNode = outgoingFlow.getTarget();
                                if (defaultFlowNode instanceof UserTask) {
                                    defaultUserTaskFlow.add((UserTask) defaultFlowNode);
                                }
                                flowNode = defaultFlowNode;
                                break;
                            }
                        }
                    } else {
                        flowNode = nextFlowNode;
                    }
                } else if (nextFlowNode instanceof InclusiveGateway) {
                    SequenceFlow defaultFlow = ((InclusiveGateway) nextFlowNode).getDefault();
                    FlowNode defaultFlowNode = defaultFlow.getTarget();
                    if (defaultFlowNode instanceof UserTask) {
                        defaultUserTaskFlow.add((UserTask) defaultFlowNode);
                    }
                    flowNode = defaultFlowNode;
                } else {
                    flowNode = nextFlowNode;
                }
            }
        }
        return defaultUserTaskFlow;
    }

    private boolean isCamundaAssigneeNotPresent(UserTask task ) {
        return task.getCamundaAssignee() == null;
    }

    private boolean isPrincipleProcessInitiator(String processInstanceId) {
        String assignee = this.camundaProcessRepository.getRuntimeVariableByNameAsString(processInstanceId, ProcessConstants.INITIATOR);
        String username = this.userService.getPrincipalIdentifier();
        if (assignee != null) {
            return assignee.equals(username);
        }

        return false;
    };
}
