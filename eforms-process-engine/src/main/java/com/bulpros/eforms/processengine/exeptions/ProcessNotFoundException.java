package com.bulpros.eforms.processengine.exeptions;

public class ProcessNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6938976342886723882L;

    public ProcessNotFoundException(String message) {
        super(message);
    }
}
