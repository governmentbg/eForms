package com.bulpros.eforms.processengine.keycloak.rest;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.bulpros.eforms.processengine.security.IAuthenticationFacade;

import lombok.RequiredArgsConstructor;

/**
 * Optional Security Configuration for Camunda REST Api.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(SecurityProperties.BASIC_AUTH_ORDER - 20)
@ConditionalOnProperty(name = "rest.security.enabled", havingValue = "true", matchIfMissing = true)
public class RestApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestApiSecurityConfigurationProperties configProps;
    private final IdentityService identityService;
    private final OAuth2AuthorizedClientService clientService;
    private final ApplicationContext applicationContext;
    private final IAuthenticationFacade iAuthenticationFacade;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(final HttpSecurity http) throws Exception {
        String jwkSetUri = this.applicationContext.getEnvironment().getRequiredProperty(
                "spring.security.oauth2.client.provider." + this.configProps.getProvider() + ".jwk-set-uri");

        http.csrf().ignoringAntMatchers("/eforms-rest/**", "/engine-rest/**", "/admin/eforms-rest/**").and().requestMatchers()
                .antMatchers("/engine-rest/**", "/eforms-rest/**", "/admin/eforms-rest/**").and().authorizeRequests()
                .antMatchers("/eforms-rest/payment/payment-status-callback").permitAll()
                .anyRequest()
                .authenticated().and().oauth2ResourceServer().jwt().jwkSetUri(jwkSetUri);
    }

    /**
     * Create a JWT decoder with issuer and audience claim validation.
     * 
     * @return the JWT decoder
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = this.applicationContext.getEnvironment().getRequiredProperty(
                "spring.security.oauth2.client.provider." + this.configProps.getProvider() + ".issuer-uri");

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(this.configProps.getRequiredAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    /**
     * Registers the REST Api Keycloak Authentication Filter.
     * 
     * @return filter registration
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Bean
    public FilterRegistrationBean keycloakAuthenticationFilter() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new KeycloakAuthenticationFilter(this.identityService, iAuthenticationFacade, this.clientService));
        filterRegistration.setOrder(102); // make sure the filter is registered after the Spring Security Filter Chain
        filterRegistration.addUrlPatterns("/engine-rest/*");
        return filterRegistration;
    }

}
