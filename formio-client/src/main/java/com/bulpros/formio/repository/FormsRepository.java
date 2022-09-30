package com.bulpros.formio.repository;


import java.util.HashMap;

import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.fasterxml.jackson.databind.JsonNode;

public interface FormsRepository {

    HashMap<String, Object> getForm(ResourcePath resourcePath, User user);
    JsonNode getResourceMetadata(String projectId, String resourcePath, User user);

}
