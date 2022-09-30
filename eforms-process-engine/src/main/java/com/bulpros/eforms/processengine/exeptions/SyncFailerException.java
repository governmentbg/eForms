package com.bulpros.eforms.processengine.exeptions;

public class SyncFailerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SyncFailerException(String message) {
        super(message);
    }
}
