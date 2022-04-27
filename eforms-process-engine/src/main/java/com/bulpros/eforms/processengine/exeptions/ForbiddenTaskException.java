package com.bulpros.eforms.processengine.exeptions;

public class ForbiddenTaskException extends RuntimeException {

    private static final long serialVersionUID = -6938976342886723884L;

    public ForbiddenTaskException(String message) {
        super(message);
    }
}
