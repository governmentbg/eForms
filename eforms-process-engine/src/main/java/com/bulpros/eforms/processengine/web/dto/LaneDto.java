package com.bulpros.eforms.processengine.web.dto;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaneDto {
    private String id;
    private String name;
    private HashMap<String, String> properties = new HashMap<>();
    private ArrayList<UserTaskDto> userTasks = new ArrayList<>();
}