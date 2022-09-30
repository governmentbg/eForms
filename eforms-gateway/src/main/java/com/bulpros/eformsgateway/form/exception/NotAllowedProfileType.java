package com.bulpros.eformsgateway.form.exception;

public class NotAllowedProfileType extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotAllowedProfileType(String message) {
        super(message);
    }
}
