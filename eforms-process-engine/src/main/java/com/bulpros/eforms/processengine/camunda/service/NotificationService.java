package com.bulpros.eforms.processengine.camunda.service;

import org.springframework.security.core.Authentication;

import java.util.Map;

public interface NotificationService {
    void sendMailNotification(String formKey, String projectId, String email, Map<String, Object> variables, Authentication authentication);
}
