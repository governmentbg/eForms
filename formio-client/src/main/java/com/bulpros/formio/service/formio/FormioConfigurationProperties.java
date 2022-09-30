package com.bulpros.formio.service.formio;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
public class FormioConfigurationProperties {
    @Value("${com.bulpros.formio.url}")
    private String url;

    @Value("${com.bulpros.formio.pdf.url}")
    private String pdfUrl;
}