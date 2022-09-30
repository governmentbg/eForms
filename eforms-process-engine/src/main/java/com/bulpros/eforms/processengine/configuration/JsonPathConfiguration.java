package com.bulpros.eforms.processengine.configuration;

import com.jayway.jsonpath.Option;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonPathConfiguration {

    @Bean
    public com.jayway.jsonpath.Configuration getJsonPathConfiguration() {
        return com.jayway.jsonpath.Configuration.builder()
                .options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS)
                .build();
    }
}
