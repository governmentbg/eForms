package com.bulpros.eforms.processengine.camunda.model;

import com.bulpros.eforms.processengine.web.dto.enums.UserTaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class UserTask {
    private String id;
    private String definitionKey;
    private String name;
    private String assignee;
    private String owner;
    private String formKey;
    private String dueDate;
    private UserTaskStatus status = UserTaskStatus.NOT_STARTED;
    private HashMap<String, String> properties = new HashMap<>();
}
