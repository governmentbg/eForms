package com.bulpros.integrations.auditlog.configuration;

import com.bulpros.integrations.auditlog.model.AuditlogClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.net.URL;

@Configuration
public class AuditlogConfiguration {

    @Value("${com.bulpros.auditlog.wsdl}")
    private URL auditlogWsdl;
    @Value("${com.bulpros.auditlog.url}")
    private String auditlogUrl;

    @Bean
    public AuditlogClient auditlogClient() {
        return AuditlogClient.create(auditlogWsdl, auditlogUrl);
    }
}
