package com.bulpros.eforms.processengine.camunda.service;

public interface ExpressionEvaluationService {

    String evaluate(String expression, String processInstanceId);

    Boolean isElExpression(String expression);
}
