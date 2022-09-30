package com.bulpros.eformsgateway.config;

import com.bulpros.eformsgateway.handler.RequestLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import reactor.core.publisher.Mono;

import java.util.List;
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
                .pathMatchers("/v2/api-docs")
                    .permitAll()
                .pathMatchers("/swagger-ui/**", "/swagger-resources/**", "/webjars/**")
                    .permitAll()
                .pathMatchers(HttpMethod.GET,"/actuator/health")
                    .permitAll()
                .pathMatchers(HttpMethod.GET,"/actuator/health")
                    .permitAll()
                .pathMatchers("/v2/api-docs")
                    .permitAll()
                .pathMatchers("/swagger-ui/**", "/swagger-resources/**", "/webjars/**")
                    .permitAll()
                .pathMatchers("/api/public/**")
                    .permitAll()
                .pathMatchers("/api/admin/caches")
                    .hasAuthority("cache-admin")
                .pathMatchers(HttpMethod.POST,"/api/admin/cases/{businessKey:[a-zA-Z0-9]*}/messages")
                .access((mono, context) -> mono.map(Authentication::getPrincipal)
                        .cast(Jwt.class)
                        .map(jwt -> (jwt.getClaimAsString("clientId") != null &&
                                jwt.getClaimAsString("clientId").equals("eforms-esb")))
                        .map(AuthorizationDecision::new))
                .pathMatchers(HttpMethod.GET, "/.well-known/openid-configuration/**")
                    .permitAll()
                .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                // Please don't remove! It is a callback request from ePayment without token !
                .pathMatchers("/api/ePayment/payment-status-callback")
                    .permitAll()
                .pathMatchers("/actuator/**")
                    .permitAll()
                .anyExchange()
                    .authenticated()
            .and()
                .addFilterAfter(getRequestLogger(), SecurityWebFiltersOrder.AUTHENTICATION)
            .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        return http.build();
    }

    @Bean
    public RequestLogger getRequestLogger() {
        return new RequestLogger();
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
