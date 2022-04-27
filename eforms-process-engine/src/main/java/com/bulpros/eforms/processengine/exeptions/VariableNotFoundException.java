package com.bulpros.eforms.processengine.exeptions;

public class VariableNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6938976342886723882L;

    public VariableNotFoundException(String message) {
        super(message);
    }
}
