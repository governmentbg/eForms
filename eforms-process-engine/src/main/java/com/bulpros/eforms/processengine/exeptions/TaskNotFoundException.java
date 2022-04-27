package com.bulpros.eforms.processengine.exeptions;

public class TaskNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6938976342886723883L;

    public TaskNotFoundException(String message) {
        super(message);
    }
}
