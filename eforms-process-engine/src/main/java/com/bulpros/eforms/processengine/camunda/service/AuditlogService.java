package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.auditlog.model.AuditlogRequest;
import com.bulpros.auditlog.model.AuditlogResponse;

public interface AuditlogService {
    AuditlogResponse registerEvent(AuditlogRequest auditlogRequest);
}
