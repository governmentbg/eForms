package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.model.CheckEDeliveryRegistrationResult;
import com.bulpros.eformsgateway.eformsintegrations.service.EDeliveryRegistrationService;
import com.bulpros.eformsgateway.form.service.ServiceService;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.web.controller.dto.ServiceSubmissionResponseDto;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceSupplierService serviceSupplierService;
    private final ServiceService serviceService;
    private final UserService userService;
    private final EDeliveryRegistrationService eDeliveryRegistrationService;

    @GetMapping(path = "/projects/{projectId}/eas/{easId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceSubmissionResponseDto> getServiceSubmissionByServiceId(Authentication authentication,
                                                                                 @PathVariable String projectId, @PathVariable String easId,
                                                                                 @RequestParam(required = false) String applicant) {
        User user = userService.getUser(authentication);
        ServiceSubmissionResponseDto response = serviceService.getServicesById(projectId, authentication, easId);
        CheckEDeliveryRegistrationResult checkEDeliveryRegistrationResult = eDeliveryRegistrationService.checkRegistration(user,
                authentication, projectId, applicant);
        response.setEDeliveryStatus(checkEDeliveryRegistrationResult.getStatus().getValue());
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projects/{projectId}/eas/{easId}/suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ResourceDto>> getServiceSuppliersByTitle(Authentication authentication,
                                                                 @PathVariable String projectId,
                                                                 @PathVariable String easId,
                                                                 @RequestParam(required = false) String title) {

        List<ResourceDto> response = serviceService.getServiceSuppliersByTitle(projectId, easId, title, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/projects/{projectId}/eas/{easId}/suppliers/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getServiceSupplierByCode(Authentication authentication,
                                                         @PathVariable String easId,
                                                         @PathVariable String projectId,
                                                         @PathVariable String code) {
        ResourceDto response = serviceSupplierService.getSupplierWithAdminUnitsByCode(projectId, authentication, easId, code);
        if (response == null) {
            throw new ResourceNotFoundException("Service with id: " + easId + " or supplier with code: "
                    + code + " are not found!");
        }
        return ResponseEntity.ok(response);
    }
}
