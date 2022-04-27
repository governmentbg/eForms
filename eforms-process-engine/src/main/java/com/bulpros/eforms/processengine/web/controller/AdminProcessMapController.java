package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.security.AssuranceLevelEnum;
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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping({ "/eforms-rest/admin/process-definition" })
public class AdminProcessMapController extends AbstractProcessMapController {

    private final ProcessService processService;
    private final ModelMapper modelMapper;
    private final UserProfileService userProfileService;

    public AdminProcessMapController(ProcessService processService, ModelMapper modelMapper,
                                UserProfileService userProfileService, UserService userService) {
        super(userService);
        this.processService = processService;
        this.modelMapper = modelMapper;
        this.userProfileService = userProfileService;
    }

    @GetMapping(path = "/{processInstanceId}/map", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProcessDto> getAdminDefaultTasks(@PathVariable String processInstanceId,
            @RequestParam(required = false) String applicant) {

        if (!isValidRequest(applicant, processInstanceId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        try {
            Process process = processService.getProcess(processInstanceId, UserStageTypeEnum.ADMINISTRATION);

            ProcessDto processDto = modelMapper.map(process, ProcessDto.class);
            return ResponseEntity.ok(processDto);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new EFormsProcessEngineException(exception.getMessage());
        }
    }

    private boolean isValidRequest(String applicant, String processInstanceId) {
        var context = processService.getProcessVariables(processInstanceId);
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String projectId = JsonPath.using(pathConfiguration).parse(context).read("$.context.formioBaseProject");
        String supplierEik = JsonPath.using(pathConfiguration).parse(context)
                .read("$.context.serviceSupplier.data.eik");

        if(!checkAssuranceLevel(context)) return false;

        if (applicant == null)
            return false;
        var userProfile = userProfileService.getUserProfileData(projectId);
        if ((userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.ServiceManager)
        || userProfileService.hasUserRole(userProfile, applicant, AdditionalProfileRoleEnum.MetadataManager))
                && applicant.equals(supplierEik)) {
            return true;
        }
        return false;
    }
}
