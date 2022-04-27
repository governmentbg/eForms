package com.bulpros.eforms.processengine.web.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ExceptionBody {
    private final int status;
    private final String error;
    private final String message;
    private Object data;

    public ExceptionBody(HttpStatus status, String message) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
    
    public ExceptionBody(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.data = data;
    }
}
