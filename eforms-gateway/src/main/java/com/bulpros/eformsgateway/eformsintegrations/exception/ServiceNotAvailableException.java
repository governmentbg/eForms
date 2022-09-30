package com.bulpros.eformsgateway.eformsintegrations.exception;

import lombok.Getter;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class ServiceNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final SeverityEnum severity;
    private final String code;
    private Object data;


    public ServiceNotAvailableException(SeverityEnum severity, String code, Object data) {
        this.severity = severity;
        this.code = code;
        this.data = data;
    }

    public ServiceNotAvailableException(SeverityEnum severity, String code) {
        this.severity = severity;
        this.code = code;
    }

    public String getFullCode() {
        return Stream.of(severity, "GATEWAY", code)
                .map(Objects::toString)
                .collect(Collectors.joining("."));
    }

}
