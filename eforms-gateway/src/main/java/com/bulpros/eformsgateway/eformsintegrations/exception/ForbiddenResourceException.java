package com.bulpros.eformsgateway.eformsintegrations.exception;

public class ForbiddenResourceException extends RuntimeException {

    private static final long serialVersionUID = -6938976342886723884L;

    public ForbiddenResourceException(String message) {
        super(message);
    }
}
