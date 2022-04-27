package com.bulpros.eformsgateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfiguration {

    @Bean(name="objectMapper")
    public ObjectMapper modelMapper() {
        return new ObjectMapper();
    }
}