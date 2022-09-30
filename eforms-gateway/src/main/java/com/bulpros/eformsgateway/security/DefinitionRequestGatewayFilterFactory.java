package com.bulpros.eformsgateway.security;

import com.bulpros.eformsgateway.cache.service.CacheService;
import com.bulpros.eformsgateway.security.dto.ResponseDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefinitionRequestGatewayFilterFactory extends AbstractCompleteRequestGatewayFilterFactory<Object> {

    public static final String GET_DEFINITION_REQUEST_CACHE = "getDefinitionRequestCache";

    private final CacheService cacheService;

    @Override
    public GatewayFilter apply(Object config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            return exchange.getPrincipal().flatMap(principal -> {
                Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);

                String fullResourcePath = uriVariables.get("resourcePath");
                String projectId = uriVariables.get("projectId");
                if (!fullResourcePath.contains("/submission") && !fullResourcePath.contains("/exists")) {

                    String cacheKey = "project/" + projectId + fullResourcePath;
                    final String cacheControl = exchange.getRequest().getHeaders().getCacheControl();

                    if (nonNull(cacheKey)) {

                        // Check if request is cached already
                        ResponseDto cachedResponse = cacheService.get(GET_DEFINITION_REQUEST_CACHE, cacheKey,
                                ResponseDto.class, cacheControl);
                        if (nonNull(cachedResponse)) {
                            // use cache
                            log.info("cachedResponse  body is -- " + cachedResponse.getBody());
                            return completeRequest(exchange, cachedResponse.getStatus(), cachedResponse.getBody());
                        }
                    }

                    final ServerHttpResponse serverHttpResponse = getServerHttpResponse(exchange, cacheKey, cacheControl);
                    return chain.filter(exchange.mutate().response(serverHttpResponse).build());
                }
                return chain.filter(exchange);
            });
        }, -2);
    }

    private ServerHttpResponse getServerHttpResponse(ServerWebExchange exchange, String cacheKey, String cacheControl) {
        final ServerHttpResponse originalResponse = exchange.getResponse();
        final HttpStatus responseStatus = exchange.getResponse().getStatusCode();
        final DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();


        return new ServerHttpResponseDecorator(originalResponse) {

            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (Objects.equals(originalResponse.getStatusCode(), HttpStatus.OK)) {
                    if (body instanceof Flux) {
                        return super.writeWith(
                            DataBufferUtils.join(body)
                                .map(dataBuffer -> {
                                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(bytes);
                                    DataBufferUtils.release(dataBuffer);
                                    if (nonNull(cacheKey)) {
                                        ResponseDto responseDto = new ResponseDto(responseStatus, bytes);
                                        cacheService.put(GET_DEFINITION_REQUEST_CACHE, cacheKey, responseDto, cacheControl);
                                    }
                                    return dataBufferFactory.wrap(bytes);
                                })
                        );
                    }
                }
                return super.writeWith(body);
            }
        };
    }
}
