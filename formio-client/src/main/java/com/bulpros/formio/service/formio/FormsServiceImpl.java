package com.bulpros.formio.service.formio;

import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.FormsRepository;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.security.FormioUserService;
import com.bulpros.formio.service.FormsService;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FormsServiceImpl implements FormsService {

    @Autowired
    private FormsRepository formsRepository;
    @Autowired
    private FormioUserService userService;

    @Override
    public HashMap<String, Object> getForm(ResourcePath resourcePath, Authentication authentication) {
        User user = this.userService.getUser(authentication);
        return formsRepository.getForm(resourcePath, user);
    }
    

    @Override
    public JsonNode getFormJson(String projectId, String resourcePath, Authentication authentication) {
        User user = this.userService.getUser(authentication);
        return formsRepository.getResourceMetadata(projectId, resourcePath, user);
    }
}
