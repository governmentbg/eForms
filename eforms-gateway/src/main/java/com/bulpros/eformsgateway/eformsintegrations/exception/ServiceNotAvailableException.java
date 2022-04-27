package com.bulpros.eformsgateway.eformsintegrations.exception;

public class ServiceNotAvailableException extends RuntimeException {
    
    public static final String ORN_NOT_AVAILABLE = "ORN_NOT_AVAILABLE";
    
    private static final long serialVersionUID = 1L;
    
    public ServiceNotAvailableException(String message) {
        super(message);
    }

}
