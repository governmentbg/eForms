package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.eforms.processengine.camunda.model.Lane;
import java.util.ArrayList;

public interface LaneService {
    ArrayList<Lane> getLanesByStageForUser(String processInstanceId, UserStageTypeEnum userStageTypeEnum);
    ArrayList<Lane> getAllLanesByStage(String processInstanceId, UserStageTypeEnum userStageTypeEnum);
}

