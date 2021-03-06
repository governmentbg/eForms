package com.bulpros.eforms.processengine.camunda.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Process {
    private String processInstanceId;
    private String businessKey;
    private ArrayList<Lane> lanes = new ArrayList<>();

    public void addLane(Lane lane) {
        this.getLanes().add(lane);
    }

}