package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.AuditlogResponse;
import com.bulpros.auditlog.repository.client.AuditlogClient;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class AuditlogServiceImpl implements AuditlogService {
    private final ConfigurationProperties configurationProperties;
    @Override
    public AuditlogResponse registerEvent(AuditlogRequest auditlogRequest) {
        var builder =  new AuditlogClient.Builder(
                UriBuilder.fromUri(configurationProperties.getIntegrationsUrl()).build());
        AuditlogClient auditlogClient = builder
                .eventTime(Calendar.getInstance().getTime())
                .eventType(auditlogRequest.getEventType())
                .eventDescription(auditlogRequest.getEventDescription())
                .build();
        return auditlogClient.getResponse();
    }
}
