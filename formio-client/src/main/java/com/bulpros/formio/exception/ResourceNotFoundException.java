package com.bulpros.formio.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6823935163283410748L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
