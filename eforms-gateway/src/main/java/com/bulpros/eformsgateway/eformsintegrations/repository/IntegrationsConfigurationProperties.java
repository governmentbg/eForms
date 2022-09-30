package com.bulpros.eformsgateway.eformsintegrations.repository;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:gateway-resource-${spring.profiles.active}.properties")
public class IntegrationsConfigurationProperties {

    @Value("${com.bulpros.integrations.url}")
    private String integrationsUrl;

    @Value("${com.bulpros.integrations.root}")
    private String integrationsRootPath;

    @Value("${com.bulpros.integrations.orn.prefix}")
    private String ornResourcePath;

    @Value("${com.bulpros.integrations.egov.prefix}")
    private String egovResourcePath;

    @Value("${com.bulpros.integrations.regix.prefix}")
    private String regixResourcePath;

    @Value("${com.bulpros.integrations.edelivery.prefix}")
    private String eDeliveryResourcePath;

    @Value("${com.bulpros.integrations.antivirus.prefix}")
    private String malwareScanResourcePath;

    @Value("${com.bulpros.integrations.etranslation.prefix}")
    private String eTranslationResourcePath;
}
