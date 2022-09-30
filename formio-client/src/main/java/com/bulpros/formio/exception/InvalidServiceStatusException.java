package com.bulpros.formio.exception;

public class InvalidServiceStatusException extends RuntimeException {

    private static final long serialVersionUID = 5344017943411519655L;

    public InvalidServiceStatusException(String message) {
        super(message);
    }
}
