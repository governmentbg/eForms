package com.bulpros.eforms.processengine.exeptions;

public class NotSatisfiedAssuranceLevel extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NotSatisfiedAssuranceLevel(String message) {
        super(message);
    }
}
