package com.bulpros.eforms.processengine.camunda.repository;

import java.util.Collection;
import java.util.Map;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component(value = "camundaProcessRepository")
@RequiredArgsConstructor
public class CamundaProcessRepositoryImpl implements CamundaProcessRepository {

    private final RepositoryService repositoryService;

    private final RuntimeService runtimeService;

    @Override
    public ActivityInstance getActivityInstanceByProcessById(String processInstanceId) {
        return this.runtimeService.getActivityInstance(processInstanceId);
    }

    @Override
    public String getProcessDefinition(String processInstanceId) {
        return getActivityInstanceByProcessById(processInstanceId).getProcessDefinitionId();
    }

    private BpmnModelInstance getModelDefinition(String processInstanceId) {
        String processDefinitionId = this.getProcessDefinition(processInstanceId);
        return this.repositoryService.getBpmnModelInstance(processDefinitionId);
    }

    @Override
    public Collection<Lane> getProcessLanes(String processInstanceId) {
        BpmnModelInstance modelInstance = this.getModelDefinition(processInstanceId);
        return modelInstance.getModelElementsByType(Lane.class);
    }

    @Override
    public Map<String,Object> getProcessVariables(String processInstance) {
        return this.runtimeService.getVariables(processInstance);
    }

    @Override
    public UserTask getUserTaskById(String processInstanceId, String taskId) {
        BpmnModelInstance modelInstance = this.getModelDefinition(processInstanceId);
        return modelInstance.getModelElementById(taskId);
    }

    @Override
    public Collection<CamundaProperty> getCamundaStaticProperties(BaseElement baseElement) {
        ExtensionElements extensionElements = baseElement.getExtensionElements();
        if (extensionElements == null) return null;

        CamundaProperties camundaProperties = extensionElements.getElementsQuery().filterByType(CamundaProperties.class)
                .singleResult();

        return camundaProperties.getCamundaProperties();
    }

    @Override
    public String getCamundaRuntimePropertyValue(BaseElement baseElement, String propertyName,
            String processInstanceId) {
        String valueByName = this.getCamundaPropertyStaticValueByName(baseElement, propertyName);
        Map<String, Object> variables = this.getProcessVariables(processInstanceId);
        return (String) variables.get(valueByName);
    }

    @Override
    public String getRuntimeVariableByNameAsString(String processInstanceId, String name) {
        return (String)this.runtimeService.getVariable(processInstanceId, name);
    }

    @Override
    public String getBusinessKey(String processInstanceId) {
        ProcessInstance processInstance = this.runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return processInstance.getBusinessKey();
    }

    @Override
    public String getCamundaPropertyStaticValueByName(BaseElement baseElement, String propertyName) {
        Collection<CamundaProperty> properties = this.getCamundaStaticProperties(baseElement);
        if (properties == null)
            return null;
        CamundaProperty property = properties.stream().filter(p -> propertyName.equalsIgnoreCase(p.getCamundaName()))
                .findFirst().orElse(null);
        return property == null ? null : property.getCamundaValue();
    }

}
