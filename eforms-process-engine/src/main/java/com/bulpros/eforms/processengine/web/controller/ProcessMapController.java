package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.exeptions.NotSatisfiedAssuranceLevel;
import com.bulpros.eforms.processengine.security.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bulpros.eforms.processengine.camunda.model.AdditionalProfileRoleEnum;
import com.bulpros.eforms.processengine.camunda.model.Process;
import com.bulpros.eforms.processengine.camunda.service.ProcessService;
import com.bulpros.eforms.processengine.camunda.service.UserProfileService;
import com.bulpros.eforms.processengine.camunda.service.UserStageTypeEnum;
import com.bulpros.eforms.processengine.web.dto.ProcessDto;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping({ "/eforms-rest/process-definition" })
public class ProcessMapController extends AbstractProcessMapController {

    private final ProcessService processService;
    private final ModelMapper modelMapper;
    private final UserProfileService userProfileService;

    public ProcessMapController(ProcessService processService, ModelMapper modelMapper,
                                UserProfileService userProfileService, UserService userService) {
        super(userService);
        this.processService = processService;
        this.modelMapper = modelMapper;
        this.userProfileService = userProfileService;
    }

    @GetMapping(path = "/{processInstanceId}/map", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProcessDto> getUserDefaultTasks(@PathVariable String processInstanceId,
            @RequestParam(required = false) String applicant) {

        try {
            if (!isValidRequest(applicant, processInstanceId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (NotSatisfiedAssuranceLevel e) {
            throw new EFormsProcessEngineException("NOT_SATISFIED_ASSURANCE_LEVEL");
        }
        try {
            Process process = processService.getProcess(processInstanceId, UserStageTypeEnum.REQUESTOR);

            ProcessDto processDto = modelMapper.map(process, ProcessDto.class);
            return ResponseEntity.ok(processDto);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(exception.getMessage());
        }
    }

    private boolean isValidRequest(String applicant, String processInstanceId) {
        String authenticatedUserEgn = userService.getPrincipalIdentifier();
        var context = processService.getProcessVariables(processInstanceId);

        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String projectId = JsonPath.using(pathConfiguration).parse(context).read("$.context.formioBaseProject");
        String caseRequestor = JsonPath.using(pathConfiguration).parse(context)
                .read("$.context.userProfile.personIdentifier");
        Map<String, String> caseApplicant = JsonPath.using(pathConfiguration).parse(context)
                .read("$.context.userProfile.applicant");

        if(!checkAssuranceLevel(context))throw new NotSatisfiedAssuranceLevel("Assurance level is not satisfied");

        var userProfile = userProfileService.getUserProfileData(projectId);
        if (applicant == null) {
            if (caseRequestor.equals(authenticatedUserEgn) && caseApplicant == null) {
                return true;
            }
        } else {
            String applicantIdentifier = JsonPath.using(pathConfiguration).parse(context)
                    .read("$.context.userProfile.applicant.identifier");
            if (this.userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.User)) {
                if (authenticatedUserEgn.equals(caseRequestor) && applicant.equals(applicantIdentifier)) {
                    return true;
                }
            }
        }
        return false;
    }
}
