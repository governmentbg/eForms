package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.form.service.ServiceService;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.model.User;
import com.bulpros.formio.repository.util.AuthenticationService;
import com.bulpros.formio.security.FormioUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Slf4j
@RequiredArgsConstructor
public class PublicServiceController {
    private final ServiceService serviceService;
    private final FormioUserService userService;

    @GetMapping(path = "/projects/{projectId}/eas/{easId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getServiceSubmissionByServiceId(@PathVariable String projectId, @PathVariable String easId) {
        var authentication = AuthenticationService.createServiceAuthentication();
        User user = userService.getUser(authentication);
        ResourceDto response = serviceService.getServiceAssuranceLevel(projectId, authentication, easId);
        return ResponseEntity.ok(response);
    }
}
