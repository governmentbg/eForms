package com.bulpros.formio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FormioObjectMapperConfiguration {

    @Bean(name="formioObjectMapper")
    public ObjectMapper modelMapper() {
        return new ObjectMapper();
    }
}