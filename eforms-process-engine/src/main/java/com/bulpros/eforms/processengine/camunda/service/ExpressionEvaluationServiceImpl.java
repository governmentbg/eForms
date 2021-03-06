package com.bulpros.eforms.processengine.camunda.service;

import camundafeel.de.odysseus.el.ExpressionFactoryImpl;
import camundafeel.de.odysseus.el.util.SimpleContext;
import camundafeel.de.odysseus.el.util.SimpleResolver;
import camundafeel.javax.el.ExpressionFactory;
import camundafeel.javax.el.PropertyNotFoundException;
import camundafeel.javax.el.ValueExpression;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ExpressionEvaluationServiceImpl implements ExpressionEvaluationService {

    @Autowired
    private RuntimeService runtimeService;

    public String evaluate(String expression, String processInstanceId) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext(new SimpleResolver());
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            context.getELResolver().setValue(context, null, variable.getKey(), variable.getValue());
        }

        ValueExpression valueExpression = factory.createValueExpression(context, expression, String.class);
        try {
            return (String) valueExpression.getValue(context);
        } catch (PropertyNotFoundException e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    public Boolean isElExpression(String expression) {
        return expression.startsWith("#{");
    }
}
