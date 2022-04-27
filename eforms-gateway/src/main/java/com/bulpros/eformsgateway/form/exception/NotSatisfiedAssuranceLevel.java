package com.bulpros.eformsgateway.form.exception;

public class NotSatisfiedAssuranceLevel extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotSatisfiedAssuranceLevel(String message) {
        super(message);
    }
}
