package com.bulpros.eforms.signature.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SignatureRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SignatureRequestException(String message) {
        super(message);
    }
}
