package com.bulpros.eforms.processengine.web.exception;

import lombok.Getter;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class EFormsProcessEngineException extends RuntimeException {

    private static final long serialVersionUID = -2776457294871155184L;

    private final SeverityEnum severity;
    private final String code;
    private Object data;

    public EFormsProcessEngineException(SeverityEnum severity, String code, Object data) {
        this.severity = severity;
        this.code = code;
        this.data = data;
    }

    public EFormsProcessEngineException(SeverityEnum severity, String code) {
        this.severity = severity;
        this.code = code;
    }

    public String getFullCode() {
        return Stream.of(severity, "PROCESS-ENGINE", code)
                .map(Objects::toString)
                .collect(Collectors.joining("."));
    }
}


