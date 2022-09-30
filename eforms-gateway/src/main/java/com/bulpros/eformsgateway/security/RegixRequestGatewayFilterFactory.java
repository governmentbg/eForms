package com.bulpros.eformsgateway.security;

import com.bulpros.eformsgateway.form.service.ConfigurationProperties;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.web.controller.dto.IdentifierTypeEnum;
import com.bulpros.eformsgateway.form.web.controller.dto.UserProfileDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static com.bulpros.eformsgateway.security.CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY;

@Component
@RequiredArgsConstructor
public class RegixRequestGatewayFilterFactory extends AbstractSubmissionRequestGatewayFilterFactory<Object> {

    @Value("${com.bulpros.formio.userprofile.project.id}")
    private String projectId;

    private final SubmissionService submissionService;
    private final UserProfileService userProfileService;
    private final ConfigurationProperties configuration;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> exchange.getPrincipal().flatMap(principal -> {
            Flux<DataBuffer> cachedBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
            String bodyAsString = "";
            if (cachedBody != null) {
                bodyAsString = bodyToString(cachedBody);
            }

            Authentication authentication = (Authentication) principal;
            UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
            String userIdentifier = userProfile.getPersonIdentifier().substring(
                    IdentifierTypeEnum.EGN.getPrefix().length() + 1);

            Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
            var documentContext = JsonPath.using(pathConfiguration).parse(bodyAsString);
            String operationName = documentContext.read("$.operation");

            var regixSubmissions = submissionService.getSubmissionsWithFilter(
                    new ResourcePath(projectId, configuration.getRegixServicesResourcePath()), authentication,
                    Collections.singletonList(
                            new SubmissionFilter(
                                    SubmissionFilterClauseEnum.IN,
                                    Collections.singletonMap(configuration.getRegixServiceOperationName(), operationName))));

            if (regixSubmissions == null || regixSubmissions.isEmpty()) {
                return completeRequest(exchange, HttpStatus.FORBIDDEN);
            }
            var regixSubmission = regixSubmissions.stream().findFirst().get();

            if ((Boolean) regixSubmission.getData().get(configuration.getRegixServicesHasRestrictions())) {
                String identifierParamPath = (String) regixSubmission.getData().get(configuration.getRegixServicesIdentifierParamPath());
                String filterIdentifier = documentContext.read("$.argument.parameters[0]." + identifierParamPath);
                if (filterIdentifier == null || !filterIdentifier.equals(userIdentifier)) {
                    return completeRequest(exchange, HttpStatus.FORBIDDEN);
                }
            }

            return chain.filter(exchange);
        });
    }

    private static String bodyToString(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            rawRef.set(Strings.fromUTF8ByteArray(bytes));
        });
        return rawRef.get();
    }
}
