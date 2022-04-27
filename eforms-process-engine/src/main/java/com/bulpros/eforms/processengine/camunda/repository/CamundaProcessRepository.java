package com.bulpros.eforms.processengine.camunda.repository;

import java.util.Collection;
import java.util.Map;

import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.model.bpmn.instance.BaseElement;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;

public interface CamundaProcessRepository {

    ActivityInstance getActivityInstanceByProcessById(String processInstanceId);

    String getProcessDefinition(String processInstanceId);

    Collection<Lane> getProcessLanes(String processInstanceId);

    Map<String,Object> getProcessVariables(String processInstance);

    UserTask getUserTaskById(String processInstanceId, String elementId);

    Collection<CamundaProperty> getCamundaStaticProperties(BaseElement baseElement);

    String getBusinessKey(String processInstanceId);

    String getCamundaPropertyStaticValueByName(BaseElement baseElement, String propertyName);

    String getCamundaRuntimePropertyValue(BaseElement baseElement, String propertyName,
            String processInstanceId);

    String getRuntimeVariableByNameAsString(String processInstanceId, String name);

}
