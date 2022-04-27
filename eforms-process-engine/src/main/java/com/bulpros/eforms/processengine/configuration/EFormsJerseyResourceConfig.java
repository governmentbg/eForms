package com.bulpros.eforms.processengine.configuration;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.springframework.stereotype.Component;

import com.bulpros.eforms.processengine.web.exception.EFormsExceptionMapper;

@Component
public class EFormsJerseyResourceConfig extends CamundaJerseyResourceConfig {
    
    private static final Set<Class<?>> ADDITIONAL_CLASSES = new HashSet<Class<?>>();
    
    static {
        ADDITIONAL_CLASSES.add(EFormsExceptionMapper.class);
    }
    
    @Override
    protected void registerAdditionalResources() {
        this.registerClasses(ADDITIONAL_CLASSES);
    }
}
