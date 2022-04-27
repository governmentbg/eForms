package com.bulpros.eformsgateway.eformsintegrations.exception;

public class UserProfileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6821234563283410748L;

    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
