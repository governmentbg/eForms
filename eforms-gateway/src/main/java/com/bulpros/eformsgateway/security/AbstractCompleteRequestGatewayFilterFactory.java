package com.bulpros.eformsgateway.security;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public abstract class AbstractCompleteRequestGatewayFilterFactory<C> extends AbstractGatewayFilterFactory<C> {
    
    protected Mono<Void> completeRequest(ServerWebExchange exchange, HttpStatus status) {
        return completeRequest(exchange, status, null);
    }
    
    protected Mono<Void> completeRequest(ServerWebExchange exchange, HttpStatus status, String body) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        if (body != null) {
            response.getHeaders().set("x-intercepted", "true");
            response.getHeaders().set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        }
        return response.setComplete();
    }

    protected URI addRequestParam(URI uri, String key, String value) {
        StringBuilder query = new StringBuilder();
        String originalQuery = uri.getRawQuery();
        if (StringUtils.hasText(originalQuery)) {
            query.append(originalQuery);
            if (originalQuery.charAt(originalQuery.length() - 1) != '&') {
                query.append('&');
            }
        }
        query.append(key + "=" + value);
        return UriComponentsBuilder.fromUri(uri).replaceQuery(query.toString()).build(true).toUri();
    }

}
