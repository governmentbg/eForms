package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.camunda.service.ProcessService;
import com.bulpros.eforms.processengine.security.AssuranceLevelEnum;
import com.bulpros.eforms.processengine.security.UserService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Map;


@Slf4j
public class AbstractProcessMapController {

    protected final UserService userService;
    protected final ProcessService processService;

    public AbstractProcessMapController(UserService userService, ProcessService processService) {
        this.userService = userService;
        this.processService = processService;
    }

    protected boolean checkAssuranceLevel(Map<String, Object> context) {
        Configuration pathConfiguration = Configuration.builder()
                .options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS).build();
        String serviceRequiredAssuranceLevel = JsonPath.using(pathConfiguration).parse(context)
                .read("$.context.service.data.requiredSecurityLevel");
        AssuranceLevelEnum requiredAssuranceLevel;
        if (serviceRequiredAssuranceLevel == null) {
            requiredAssuranceLevel = AssuranceLevelEnum.NONE;
        } else {
            try {
                requiredAssuranceLevel = AssuranceLevelEnum.valueOf(serviceRequiredAssuranceLevel.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                log.warn("Assurance Level: " + serviceRequiredAssuranceLevel + " is not valid value");
                return false;
            }
        }
        var userAssuranceLevel = userService.getPrincipalAssuranceLevel();
        return userAssuranceLevel.getLevel() >= requiredAssuranceLevel.getLevel();
    }

}
