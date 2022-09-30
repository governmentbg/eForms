package com.bulpros.eforms.processengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableRetry
@EnableAsync(proxyTargetClass = true)
public class EFormsProcessEngineApplication {

    public static void main(String... args) {
        SpringApplication.run(EFormsProcessEngineApplication.class, args);
    }
}