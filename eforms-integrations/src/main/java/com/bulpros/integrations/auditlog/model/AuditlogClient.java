package com.bulpros.integrations.auditlog.model;

import lombok.extern.slf4j.Slf4j;

import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.List;

@Slf4j
public class AuditlogClient {
    private RegisterEventSOAPPort auditlog;

    public static AuditlogClient create(URL auditlogWsdl, String urlLocation) {
        RegisterEventSOAPPort port = new BGeGovEventsServices(auditlogWsdl).getRegisterEventSOAPPortImplPort();
        BindingProvider bindingProvider = (BindingProvider) port;
        bindingProvider.getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlLocation);
        return new AuditlogClient(port);
    }

    public AuditlogClient(RegisterEventSOAPPort auditlog) {
        this.auditlog = auditlog;
    }

    public AuditlogResponse registerEvent(List<EAServiceLCEventType> eaServiceLCEvent) {
        try {
            auditlog.registerEvent(eaServiceLCEvent);
            return new AuditlogResponse("Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new AuditlogResponse("Error");
        }
    }
}
