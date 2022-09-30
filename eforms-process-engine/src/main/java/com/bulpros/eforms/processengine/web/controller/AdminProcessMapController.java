package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.camunda.model.Process;
import com.bulpros.eforms.processengine.camunda.model.enums.AdditionalProfileRoleEnum;
import com.bulpros.eforms.processengine.camunda.model.enums.UserStageTypeEnum;
import com.bulpros.eforms.processengine.camunda.service.ProcessService;
import com.bulpros.eforms.processengine.camunda.service.UserProfileService;
import com.bulpros.eforms.processengine.exeptions.NotSatisfiedAssuranceLevel;
import com.bulpros.eforms.processengine.security.UserService;
import com.bulpros.eforms.processengine.web.dto.ProcessDto;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.SeverityEnum;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping({"/eforms-rest/admin/process-definition"})
public class AdminProcessMapController extends AbstractProcessMapController {

    private final ModelMapper modelMapper;
    private final UserProfileService userProfileService;
    private final Configuration jsonPathConfiguration;

    public AdminProcessMapController(ModelMapper modelMapper, UserProfileService userProfileService,
                                     UserService userService, ProcessService processService,
                                     Configuration jsonPathConfiguration) {
        super(userService, processService);
        this.modelMapper = modelMapper;
        this.userProfileService = userProfileService;
        this.jsonPathConfiguration = jsonPathConfiguration;

    }

    @Timed(value = "eforms-process-engine-get-admin-default-tasks.time")
    @GetMapping(path = "/{processInstanceId}/map", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProcessDto> getAdminDefaultTasks(@PathVariable String processInstanceId,
                                                    @RequestParam(required = false) String applicant) {

        try {
            if (!isValidRequest(applicant, processInstanceId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } catch (NotSatisfiedAssuranceLevel e) {
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "NOT_SATISFIED_ASSURANCE_LEVEL");
        }
        try {
            Process process = processService.getProcess(processInstanceId, UserStageTypeEnum.ADMINISTRATION);

            ProcessDto processDto = modelMapper.map(process, ProcessDto.class);
            return ResponseEntity.ok(processDto);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(SeverityEnum.ERROR, "MAP", exception.getMessage());
        }
    }

    private boolean isValidRequest(String applicant, String processInstanceId) {
        var context = processService.getProcessVariables(processInstanceId);
        DocumentContext documentContext = JsonPath.using(jsonPathConfiguration).parse(context);
        String projectId = documentContext.read("$.context.formioBaseProject");
        String supplierEik = documentContext.read("$.context.serviceSupplier.data.eik");

        if (!checkAssuranceLevel(context)) throw new NotSatisfiedAssuranceLevel("Assurance level is not satisfied");

        if (applicant == null)
            return false;
        var userProfile = userProfileService.getUserProfileData(projectId);
        return (userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.ServiceManager)
                || userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.MetadataManager))
                && applicant.equals(supplierEik);
    }
}
