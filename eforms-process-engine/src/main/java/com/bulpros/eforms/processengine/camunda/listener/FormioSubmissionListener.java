package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.delegate.*;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import javax.inject.Named;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Named("formioSubmissionListener")
@Scope("prototype")
public class FormioSubmissionListener implements TaskListener {

    @Value("${keycloak.user.id.property}")
    private String keycloakUserIdProperty;
    @Value("${com.bulpros.process-admin.group}")
    private String processAdminGroupId;

    private static final String CREATE = "CREATE";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String PARAMETERS_MAP_VARIABLE = "parameters";

    private static String EXPRESSION_PATTERN = "(?<expression>^[\\\"\\\\#\\\\{].*[\\\\}\\\"$])";

    private final SubmissionService submissionService;

    private Expression project;
    private Expression projectValueType;
    private Expression resource;
    private Expression resourceValueType;
    private Expression method;
    private Expression payload;
    private Expression submissionId;
    private Expression response;

    @Override
    public void notify(DelegateTask delegateTask) {
        try {
            String project = (String) this.getProject().getValue(delegateTask.getExecution());
            String projectType = (String) this.getProjectValueType().getValue(delegateTask.getExecution());
            ValueTypeEnum projectTypeEnum = ValueTypeEnum.valueOf(projectType.toUpperCase(Locale.ROOT));
            String resource = (String) this.getResource().getValue(delegateTask.getExecution());
            String resourceType = (String) this.getResourceValueType().getValue(delegateTask.getExecution());
            ValueTypeEnum resourceTypeEnum = ValueTypeEnum.valueOf(resourceType.toUpperCase(Locale.ROOT));
            String method = (String) this.getMethod().getValue(delegateTask.getExecution());
            String payload = null;
            if (this.payload != null && this.getPayload() != null) {
                payload = (String) this.getPayload().getValue(delegateTask.getExecution());
            }
            String submissionIdValue = null;
            if (this.submissionId != null && this.getSubmissionId() != null) {
                submissionIdValue = (String) this.getSubmissionId().getValue(delegateTask.getExecution());
            }
            String response = null;
            if (this.response != null && this.getResponse() != null) {
                response = (String) this.getResponse().getValue(delegateTask.getExecution());
            }

            Map<String, Object> parameters = (Map<String, Object>) delegateTask.getExecution().getVariable(PARAMETERS_MAP_VARIABLE);

            Authentication authentication = AuthenticationService.createServiceAuthentication();
            ResourceDto result = null;
            String submissionData;
            switch (method) {
                case CREATE:
                    submissionData = getSubmissionData(payload, parameters, delegateTask.getExecution());
                    result = this.submissionService.createSubmission(project, projectTypeEnum, resource, resourceTypeEnum, authentication, submissionData);
                    break;
                case UPDATE:
                    submissionData = getSubmissionData(payload, parameters, delegateTask.getExecution());
                    String patchData = this.createPatchData(submissionData);
                    result = this.submissionService.updateSubmission(project, projectTypeEnum, resource, resourceTypeEnum, authentication, submissionIdValue, patchData);
                    break;
                case DELETE:
                    result = this.submissionService.deleteSubmission(project, projectTypeEnum, resource, resourceTypeEnum, authentication, submissionIdValue);
                    break;
            }
            if (response != null) {
                delegateTask.getExecution().setVariable(response, result);
            }
        } catch (RestClientResponseException exception) {
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "FORMIO.COMMUNICATION", exception.getMessage());
        } catch (RestClientException exception) {
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "FORMIO.UNAVAILABLE", exception.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "FORMIO.COMMUNICATION", e.getMessage());
        }
    }

    private String getSubmissionData(String payload, Map<String, Object> parameters, DelegateExecution delegateExecution) throws Exception {
        ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        JSONObject jsonObject = (JSONObject) parser.parse(payload);
        JSONObject dataObject = (JSONObject) jsonObject.get("data");
        Set<String> keys = dataObject.keySet();
        for (String key : keys) {
            Object value = dataObject.get(key);

            Matcher result = Pattern.compile(EXPRESSION_PATTERN).matcher(value.toString());
            if (result.find()) {
                Object evaluatedValue;
                String expressionValue = StringUtils.substringBetween(value.toString(), "#{", "}");
                if (parameters != null && parameters.get(expressionValue) != null) {
                    evaluatedValue = expressionManager
                            .createExpression("#{" + parameters.get(expressionValue).toString() + "}")
                            .getValue(delegateExecution);
                } else {
                    evaluatedValue = expressionManager.createExpression(value.toString()).getValue(delegateExecution);
                }

                if (evaluatedValue instanceof String) {
                    dataObject.put(key, (String) evaluatedValue);
                } else if (evaluatedValue instanceof Date) {
                    dataObject.put(key, new DateTime(((Date) evaluatedValue).getTime()).toString());
                } else if (evaluatedValue instanceof Number) {
                    dataObject.put(key, String.valueOf(evaluatedValue));
                } else if (evaluatedValue instanceof DateTime) {
                    dataObject.put(key, ((DateTime) evaluatedValue).toString());
                } else {
                    dataObject.put(key, evaluatedValue);
                }
            } else {
                dataObject.put(key, value);
            }
        }
        return jsonObject.toString();
    }

    private String createPatchData(String payload) {
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(payload);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject dataObject = (JSONObject) jsonObject.get("data");
        JSONArray jsonArray = new JSONArray();
        Set<String> keys = dataObject.keySet();
        for (String key : keys) {
            Object value = dataObject.get(key);
            JSONObject newObject = DataUtil.getJsonObjectForPatch("add", "/data/" + key, value);
            jsonArray.add(newObject);
        }
        return jsonArray.toString();
    }
}
