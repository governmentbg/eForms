package com.bulpros.eforms.processengine.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionBody {

    private int status;
    private String error;
    private String message;
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
