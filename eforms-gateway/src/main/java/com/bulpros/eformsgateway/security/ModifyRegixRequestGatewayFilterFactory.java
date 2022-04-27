package com.bulpros.eformsgateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class ModifyRegixRequestGatewayFilterFactory extends AbstractCompleteRequestGatewayFilterFactory<Object> {

    public ModifyRegixRequestGatewayFilterFactory() {
        super();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .map(principal -> ((JwtAuthenticationToken) principal).getToken().getClaimAsString("personIdentifier"))
                .flatMap(pin -> {
                    var claimsEgn = pin.substring(pin.indexOf("-") + 1);
                    var egn = exchange.getRequest().getQueryParams().getFirst("egn");
                    if (egn != null && egn.isEmpty()) {
                        return completeRequest(exchange, HttpStatus.BAD_REQUEST);
                    } else if (egn != null) {
                        return claimsEgn.equals(egn) ? chain.filter(exchange) : completeRequest(exchange, HttpStatus.FORBIDDEN);
                    } else {
                        return chain.filter(exchange);
                    }
                });
    }
}
