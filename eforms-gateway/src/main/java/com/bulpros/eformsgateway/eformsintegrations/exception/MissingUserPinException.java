package com.bulpros.eformsgateway.eformsintegrations.exception;

public class MissingUserPinException extends RuntimeException {
    private static final long serialVersionUID = -6821244563283410748L;

    public MissingUserPinException(String message) {
        super(message);
    }
}
