package com.bulpros.eforms.processengine.camunda.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public class Lane {
    private String id;
    private String name;
    private HashMap<String, String> properties = new HashMap<>();
    private ArrayList<UserTask> userTasks = new ArrayList<>();

    public void addUserTask(UserTask userTask) {
        this.getUserTasks().add(userTask);
    }
}