package com.bulpros.eformsgateway.handler;


import com.bulpros.eformsgateway.process.repository.utils.ProcessConstants;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RequestLogger implements WebFilter {

    private static final String CONTEXT_KEY = "CONTEXT_KEY";
    private static final String AUTHORIZATION = "authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        return chain.filter(exchange)
                .doOnEach(logOnEach(request))
                .contextWrite(Context.of(CONTEXT_KEY, request.getHeaders()));
    }

    private Consumer<? super Signal<Void>> logOnEach(ServerHttpRequest request) {
        return signal -> {
            Optional<List<String>> optionalAuth = Optional.ofNullable(request.getHeaders().get(AUTHORIZATION));
            optionalAuth.ifPresent(headers -> {
                String token = headers
                        .stream()
                        .limit(1)
                        .map(t -> t.substring(t.indexOf(" ") + 1))
                        .collect(Collectors.joining());
                String username = extractUsernameFromTokenClaims(token);
                String msg = MessageFormat.format("HTTP-Method: {0} URL: {1} IP-Address: {2} Username: {3}",
                        request.getMethod(),
                        request.getURI(),
                        request.getRemoteAddress(),
                        username);

                log.info(msg);
            });
        };
    }

    private static String extractUsernameFromTokenClaims(String token) {
        try {
            JWT jwt = JWTParser.parse(token);
            if (jwt.getJWTClaimsSet().getClaim("clientId") != null &&
                    jwt.getJWTClaimsSet().getClaim("clientId").toString().equals("eforms-esb")) {
                return "esb";
            }
            return jwt.getJWTClaimsSet().getClaim(ProcessConstants.PREFERRED_USERNAME).toString();
        } catch (ParseException e) {
            String msg = MessageFormat.format("Unable to parse token. {0}", e.getMessage());
            log.error(msg);
        }
        return null;
    }
}
