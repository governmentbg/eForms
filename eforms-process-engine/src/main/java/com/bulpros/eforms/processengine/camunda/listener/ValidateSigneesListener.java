package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.model.UserProfileDto;
import com.bulpros.eforms.processengine.camunda.service.IdentifierTypeEnum;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import spinjar.com.jayway.jsonpath.JsonPath;
import spinjar.com.jayway.jsonpath.internal.JsonContext;
import spinjar.com.minidev.json.JSONArray;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateSigneesListener implements TaskListener {

    private final FormService formService;
    private final SubmissionService submissionService;
    private final ConfigurationProperties configuration;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final AuthenticationFacade authenticationFacade;
    private final UserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        final Authentication authentication = this.authenticationFacade.getAuthentication();
        final ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        final TaskFormData taskFormData = this.formService.getTaskFormData(delegateTask.getId());
        final String formKey = taskFormData.getFormKey();
        String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(formKey);
        JacksonJsonNode submissionJson = (JacksonJsonNode)
                Spin.JSON(delegateTask.getVariable(ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey));
        final String projectId = (String) expressionManager
                .createExpression(configuration.getProjectIdPathExpr())
                .getValue(delegateTask.getExecution());

        JsonContext jsonContext = (JsonContext) JsonPath.parse(submissionJson.toString());

        String requiredSignatures = jsonContext.read(configuration.getFormSigneesRequiredSignaturesJsonPathQuery());
        if (RequiredSignaturesEnum.REQUESTOR.equals(RequiredSignaturesEnum.getByValue(requiredSignatures))) {
            return;
        }

        JSONArray jsonArray = jsonContext.read(configuration.getFormSigneesIdentifiersJsonPathQuery());
        List<String> missingProfiles = new ArrayList<>();
        List<UserProfileDto> foundProfiles = new ArrayList<>();
        if (jsonArray.isEmpty()) {
            log.warn("List of signees is expected but none was found.");
            throw new EFormsProcessEngineException("EMPTY_SIGNEES_LIST");
        }
        String[] signees;
        try {
            signees = objectMapper.readValue(jsonArray.toString(), String[].class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new EFormsProcessEngineException(String.format("Couldn't read signees: %s", e.getMessage()));
        }
        for (String signee : signees) {
            String identifier = IdentifierTypeEnum.PERSONAL_NUMBER.getIdentifierFromNumber(signee);

            if (identifier.equalsIgnoreCase(userService.getPrincipalIdentifier())) {
                log.info("Currently logged user was found in the list of signees and was ignored.");
                continue;
            }

            List<SubmissionFilter> filters = Collections.singletonList(
                    new SubmissionFilter(
                            SubmissionFilterClauseEnum.NONE,
                            Map.of(configuration.getUserIdPropertyKey(), identifier,
                                   configuration.getUserIsActivePropertyKey(), true)));
            List<ResourceDto> userProfiles = new ArrayList<>();
            try {
                userProfiles = submissionService.getSubmissionsWithFilter(
                        new ResourcePath(projectId, configuration.getUserProfilePath()), authentication, filters);
            } catch (Exception ex) {
                log.warn(String.format("Couldn't get user profile: %s", identifier));
            }

            if (userProfiles.isEmpty()) {
                missingProfiles.add(signee);
            } else {
                Map<String, Object> userData = userProfiles.get(0).getData();
                UserProfileDto userProfileDto = modelMapper.map(userData, UserProfileDto.class);
                foundProfiles.add(userProfileDto);
            }
        }
        if (!missingProfiles.isEmpty()) {
            log.warn(String.format("The following person identifiers have no active profiles but are selected as signees: %s", String.join(", ", missingProfiles)));
            throw new EFormsProcessEngineException("MISSING_SIGNEES_PROFILES", missingProfiles);
        } else if (foundProfiles.isEmpty()) {
            log.warn("List of signees is expected but none was found.");
            throw new EFormsProcessEngineException("EMPTY_SIGNEES_LIST");
        } else {
            try {
                jsonContext.put("$.data", "signeesProfiles", foundProfiles);
            } catch (Exception ex) {
                log.warn("Couldn't add user profiles to submission data.");
            }
            delegateTask.setVariable(ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey, jsonContext.json());
        }
        final String processInstanceId = delegateTask.getExecution().getProcessInstanceId();
        final String publicPortalCurrentTaskUrl = String.format(configuration.getPublicPortalCurrentTaskUrl(), processInstanceId);
        final String signTaskUrl = configuration.getPublicPortalUrl() + publicPortalCurrentTaskUrl;
        delegateTask.setVariable("signTaskUrl", signTaskUrl);
    }

}
