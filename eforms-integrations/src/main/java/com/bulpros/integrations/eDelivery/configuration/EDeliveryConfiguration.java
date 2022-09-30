package com.bulpros.integrations.eDelivery.configuration;

import com.bulpros.integrations.eDelivery.model.EDeliveryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;

import java.net.URL;

@Configuration
public class EDeliveryConfiguration {

    @Value("${com.bulpros.eDelivery.wsdl}")
    private URL eDeliveryWsdl;
    @Value("${com.bulpros.eDelivery.keyStore.file}")
    private FileSystemResource eDeliveryKeystoreFile;
    @Value("${com.bulpros.eDelivery.keyStore.pass}")
    private String eDeliveryKeystorePass;
    @Value("${com.bulpros.eDelivery.keyStore.type}")
    private String eDeliveryKeystoreType;

    @Bean
    public EDeliveryClient eDeliveryClient() {
        return EDeliveryClient.create(eDeliveryWsdl, eDeliveryKeystoreFile, eDeliveryKeystorePass, eDeliveryKeystoreType);
    }
}
