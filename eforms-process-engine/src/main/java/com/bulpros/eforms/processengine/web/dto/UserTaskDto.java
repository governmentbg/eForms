package com.bulpros.eforms.processengine.web.dto;

import java.util.HashMap;

import com.bulpros.eforms.processengine.web.dto.enums.UserTaskStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTaskDto {
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
