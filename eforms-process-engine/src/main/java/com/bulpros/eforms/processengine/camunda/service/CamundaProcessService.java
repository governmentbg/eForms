package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.Lane;
import com.bulpros.eforms.processengine.camunda.model.Process;
import com.bulpros.eforms.processengine.camunda.repository.CamundaProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@RequiredArgsConstructor
@Service(value = "camundaProcessService")
public class CamundaProcessService implements ProcessService {

    private final CamundaLaneService camundaLaneService;
    private final CamundaProcessRepository camundaProcessRepository;

    @Override
    public Process getProcess(String processInstanceId, UserStageTypeEnum userStageTypeEnum) {
        ArrayList<Lane> lanes = camundaLaneService.getLanesByStageForUser(processInstanceId, userStageTypeEnum);
        return getProcessWithKeyAndLanes(processInstanceId, lanes);
    }

    @Override
    public Process getProcessWithNoDefaultUser(String processInstanceId, UserStageTypeEnum userStageTypeEnum) {
        ArrayList<Lane> lanes = camundaLaneService.getAllLanesByStage(processInstanceId, userStageTypeEnum);
        return getProcessWithKeyAndLanes(processInstanceId, lanes);
    }

    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return camundaProcessRepository.getProcessVariables(processInstanceId);
    }

    private Process getProcessWithKeyAndLanes(String processInstanceId, ArrayList<Lane> lanes) {
        Process process = new Process();
        process.setProcessInstanceId(processInstanceId);
        process.setBusinessKey(camundaProcessRepository.getBusinessKey(processInstanceId));
        process.setLanes(lanes);
        return process;
    }

}

