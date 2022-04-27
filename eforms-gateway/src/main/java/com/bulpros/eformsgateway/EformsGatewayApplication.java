package com.bulpros.eformsgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ConfigurationPropertiesScan("com.bulpros.eformsgateway")
@EnableRetry
public class EformsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EformsGatewayApplication.class, args);
	}

}
