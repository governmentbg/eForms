package com.bulpros.eformsgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration implements WebFluxConfigurer{

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers("/auth/**")
                    .permitAll()
                .pathMatchers("/api/public/**")
                    .permitAll()
                .pathMatchers("/api/admin/caches")
                    .hasAuthority("cache-admin")
                .pathMatchers(HttpMethod.GET, "/.well-known/openid-configuration/**")
                    .permitAll()
                .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                // Please don't remove! It is a callback request from ePayment without token !
                .pathMatchers("/api/ePayment/payment-status-callback")
                    .permitAll()
                // TODO remove after applying endpoint security rules
                .pathMatchers("/api/terminate-process/**")
                    .denyAll()
                .anyExchange()
                    .authenticated()
            .and()
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        return http.build();
    }

    @Bean
    @SuppressWarnings("unchecked")
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> ((List<String>)  jwt.getClaims().get("roles")).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}
