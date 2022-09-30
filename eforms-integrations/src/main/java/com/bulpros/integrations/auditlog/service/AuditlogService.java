package com.bulpros.integrations.auditlog.service;

import com.bulpros.integrations.auditlog.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@Component("auditlogService")
@Slf4j
public class AuditlogService {

    @Value("${com.bulpros.auditlog.eforms.oid}")
    private String eFormsOID;

    private final AuditlogClient auditlogClient;

    public AuditlogService(AuditlogClient auditlogClient) {
        this.auditlogClient = auditlogClient;
    }

    public AuditlogResponse registerEvent(AuditlogRequest event) throws Exception {
        List<EAServiceLCEventType> request = new ArrayList<>();
        request.add(composeRequest(event));
        return auditlogClient.registerEvent(request);
    }

    private EAServiceLCEventType composeRequest(AuditlogRequest event) throws Exception {
        EAServiceLCEventType auditlogEvent = new EAServiceLCEventType();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(event.getEventTime() != null ? event.getEventTime() :
                Calendar.getInstance().getTime());
        XMLGregorianCalendar xmlEventTime = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(gregorianCalendar);
        auditlogEvent.setEventTime(xmlEventTime);
        auditlogEvent.setEventType(event.getEventType());
        auditlogEvent.setEventType2("");
        auditlogEvent.setInformationSystemOID(eFormsOID);
        if (StringUtils.isNotEmpty(event.getEventDescription())) {
            auditlogEvent.setEventDescription(event.getEventDescription());
        } else {
            auditlogEvent.setEventDescription(event.getEventType().name());
        }

        RequestedServiceType requestedService = new RequestedServiceType();
        requestedService.setServiceOID(eFormsOID);
        requestedService.setSPOID(eFormsOID);
        if (event.getService() != null) {
            if (StringUtils.isNotEmpty(event.getService().getServiceOID())) {
                requestedService.setServiceOID(event.getService().getServiceOID());
            }
            requestedService.setSPName(event.getService().getSpName());
            requestedService.setServiceName(event.getService().getServiceName());
            requestedService.setAuthnRequesterOID(event.getService().getAuthnRequesterOID());
            requestedService.setAdminiOID(event.getService().getAdminiOID());
            requestedService.setAdminLegalName(event.getService().getAdminLegalName());
        }
        auditlogEvent.setRequestedService(requestedService);

        auditlogEvent.setAuthnReqID(event.getAuthnReqID());
        auditlogEvent.setAuthnRespID(event.getAuthnRespID());
        auditlogEvent.setDocumentRegId(event.getDocumentRegId());

        return auditlogEvent;
    }
}
