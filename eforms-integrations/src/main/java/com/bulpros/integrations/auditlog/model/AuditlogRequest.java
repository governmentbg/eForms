package com.bulpros.integrations.auditlog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditlogRequest {

    private Date eventTime;
    private EventTypeEnum eventType;
    //private String informationSystemOID;
    private String eventDescription;

    private AuditlogServiceRequest service;

    private String authnReqID;
    private String authnRespID;
    private String documentRegId;

}
