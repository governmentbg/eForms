package com.bulpros.integrations.configuration;

import com.bulpros.integrations.configuration.esb.EsbTokenResponseConverter;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyStore;
import java.util.Arrays;

@Configuration
public class RestTemplateConfiguration {

    @Value("${com.bulpros.borica.keyStore.file}")
    private FileSystemResource boricaKeystoreFile;
    @Value("${com.bulpros.borica.keyStore.pass}")
    private String boricaKeystorePass;
    @Value("${com.bulpros.borica.keyStore.type}")
    private String boricaKeystoreType;

    @Value("${com.bulpros.esb.keyStore.file}")
    private FileSystemResource esbKeystoreFile;
    @Value("${com.bulpros.esb.keyStore.pass}")
    private String esbKeystorePass;
    @Value("${com.bulpros.esb.keyStore.type}")
    private String esbKeystoreType;

    @Value("${com.bulpros.etranslation.url}")
    private String eTranslationUrl;
    @Value("${com.bulpros.etranslation.application}")
    private String eTranslationUsername;
    @Value("${com.bulpros.etranslation.password}")
    private String eTranslationPassword;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public RestTemplate restTemplateBorica(RestTemplateBuilder restTemplateBuilder) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(boricaKeystoreType);
        keyStore.load(boricaKeystoreFile.getInputStream(), boricaKeystorePass.toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, boricaKeystorePass.toCharArray())
                .build();

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        return restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }

    @Bean("restTemplateEsbToken")
    public RestTemplate restTemplateEsbToken(RestTemplateBuilder restTemplateBuilder) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(esbKeystoreType);
        keyStore.load(esbKeystoreFile.getInputStream(), esbKeystorePass.toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, esbKeystorePass.toCharArray())
                .build();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new EsbTokenResponseConverter());

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        return restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .messageConverters(Arrays.asList(
                        new FormHttpMessageConverter(), tokenResponseHttpMessageConverter))
                .build();
    }

    @Bean("restTemplateEsb")
    public RestTemplate restTemplateEsbRequest(RestTemplateBuilder restTemplateBuilder) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(esbKeystoreType);
        keyStore.load(esbKeystoreFile.getInputStream(), esbKeystorePass.toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, esbKeystorePass.toCharArray())
                .build();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new EsbTokenResponseConverter());

        HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
        return restTemplateBuilder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(client))
                .build();
    }

    @Bean
    public HttpPost httpPost() {
        UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials(eTranslationUsername, eTranslationPassword);
        HttpPost post = new HttpPost(eTranslationUrl);
        return post;
    }

    @Bean("eTranslationCredentialProvider")
    public CredentialsProvider credentialsProvider() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(eTranslationUsername, eTranslationPassword));
        return credentialsProvider;
    }

    @Bean("eTranslationHttpClient")
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider()).setRedirectStrategy(new DefaultRedirectStrategy())
                .build();
    }
}
