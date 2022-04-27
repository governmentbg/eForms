package com.bulpros.eforms.processengine.web.exception;

import lombok.Getter;

@Getter
public class EFormsProcessEngineException extends RuntimeException {

    private static final long serialVersionUID = -2776457294871155184L;
    
    private Object data;

    public EFormsProcessEngineException(String message) {
        super(message);
    }
    
    public EFormsProcessEngineException(String message, Object data) {
        super(message);
        this.data = data;
    }
}
