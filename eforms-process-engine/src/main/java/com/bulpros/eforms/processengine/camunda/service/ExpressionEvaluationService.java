package com.bulpros.eforms.processengine.camunda.service;

public interface ExpressionEvaluationService {

    String evaluateString(String expression, String processInstanceId);

    Boolean evaluateBoolean(String expression, String processInstanceId);

    Boolean isElExpression(String expression);
}
