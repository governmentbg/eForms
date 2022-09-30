package com.bulpros.formio.exception;

public class NotActiveException extends RuntimeException {

    private static final long serialVersionUID = 6150675158584949773L;

    public NotActiveException(String message) {
        super(message);
    }
}
