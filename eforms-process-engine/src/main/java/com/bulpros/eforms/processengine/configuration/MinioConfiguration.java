package com.bulpros.eforms.processengine.configuration;

import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import io.minio.MinioClient;
import io.minio.credentials.Provider;
import io.minio.credentials.WebIdentityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

@Configuration
@Lazy(value = true)
@RequiredArgsConstructor
public class MinioConfiguration {

    @Value("${com.bulpros.minio.url}")
    private String minioUrl;
    @Value("${com.bulpros.minio.accessKey}")
    private String minioAccessKey;
    @Value("${com.bulpros.minio.password}")
    private String minioUrlPass;

    private final AuthenticationFacade authenticationFacade;

    @Bean("stsClient")
    @Scope("prototype")
    public MinioClient getAuthenticatedUserMinioClient() {
        Jwt jwt =
                (Jwt) authenticationFacade.getAuthentication().getPrincipal();
        Provider provider =
                new WebIdentityProvider(
                        () -> new io.minio.credentials.Jwt(jwt.getTokenValue(),
                                Long.valueOf(Objects.requireNonNull(jwt.getExpiresAt()).getEpochSecond()).intValue()),
                        minioUrl,
                        null,
                        null,
                        null,
                        null,
                        null);

        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentialsProvider(provider)
                .build();
    }

    @Bean("staticClient")
    public MinioClient getAdminMinioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioUrlPass)
                .build();
    }

}
