package com.bulpros.formio.service;

import com.bulpros.formio.repository.formio.ResourcePath;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

public interface FormsService {

    HashMap<String, Object> getForm(ResourcePath resourcePath, Authentication authentication);
    JsonNode getFormJson(String projectId, String resourcePath, Authentication authentication);

}
