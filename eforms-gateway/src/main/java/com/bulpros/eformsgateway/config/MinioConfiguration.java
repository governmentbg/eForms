package com.bulpros.eformsgateway.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    @Value("${com.bulpros.minio.url}")
    private String minioUrl;
    @Value("${com.bulpros.minio.accessKey}")
    private String minioAccessKey;
    @Value("${com.bulpros.minio.password}")
    private String minioUrlPass;


    @Bean()
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioUrlPass)
                .build();
    }

}
