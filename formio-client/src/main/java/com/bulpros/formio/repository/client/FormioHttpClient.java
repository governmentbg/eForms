package com.bulpros.formio.repository.client;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.util.GenerateTokenUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;


@SuppressWarnings({"rawtypes", "unchecked"})
public class FormioHttpClient {
    private RestTemplate restTemplate;
    private URI url;
    private HttpMethod method = HttpMethod.GET;
    private HttpEntity<String> entity;
    private ParameterizedTypeReference resultType;
    private Authentication authentication;

    private FormioHttpClient(URI url){
        this.url = url;
    }

    public static class Builder {
        private RestTemplate restTemplate;
        private URI url;
        private HttpMethod method = HttpMethod.GET;
        private HttpEntity<String> entity;
        private ParameterizedTypeReference resultType;
        private Authentication authentication;

        public Builder(RestTemplate restTemplate, URI url) {
            this.restTemplate = restTemplate;
            this.url = url;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder entity(HttpEntity<String> entity) {
            this.entity = entity;
            return this;
        }

        public Builder returnType(ParameterizedTypeReference resultType){
            this.resultType = resultType;
            return this;
        }

        public Builder withAuthentication(Authentication authentication){
            this.authentication = authentication;
            return this;
        }

        public FormioHttpClient build() {
            FormioHttpClient requestSender = new FormioHttpClient(this.url);
            requestSender.url = this.url;
            requestSender.method = this.method;
            requestSender.entity = this.entity;
            requestSender.resultType = this.resultType;
            requestSender.authentication = this.authentication;
            requestSender.restTemplate = this.restTemplate;
            return  requestSender;
        }
    }

    public ResponseEntity<List<ResourceDto>> getListResult(User user){
        createEntityObject(user);
        var result = this.restTemplate.exchange(url, this.method, entity , resultType);
        return result;
    }

    public ResponseEntity<ResourceDto> getResult(User user){
        createEntityObject(user);
        var result = this.restTemplate.exchange(url, this.method, entity , resultType);
        return result;
    }

    public ResponseEntity<JsonNode> getJsonResult(User user){
        createEntityObject(user);
        var result = this.restTemplate.exchange(url, this.method, entity , resultType);
        return result;
    }

    public ResponseEntity<Object> getObject(User user){
        createEntityObject(user);
        var result = this.restTemplate.exchange(url, this.method, entity , resultType);
        return result;
    }

    private HttpEntity<String> createEntityObject(User user){
        String token = GenerateTokenUtil.createFormioJWT(user);
        if(this.entity == null){
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-jwt-token", token);
            this.entity = new HttpEntity<>(null, headers);
            var body = entity.getBody();
        }
        else {
            HttpHeaders existingHeaders = this.entity.getHeaders();
            String existingBody = this.entity.getBody();
            HttpHeaders headers = new HttpHeaders();
            headers.addAll(existingHeaders);
            headers.add("x-jwt-token", token);
            this.entity = new HttpEntity<>(existingBody, headers);
        }
        return entity;
    }
}
