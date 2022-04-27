package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.eformsintegrations.model.EDeliveryStatusEnum;
import com.bulpros.eformsgateway.eformsintegrations.service.LegalPersonRegistrationService;
import com.bulpros.eformsgateway.eformsintegrations.service.SubjectRegistrationService;
import com.bulpros.eformsgateway.form.service.ServiceService;
import com.bulpros.eformsgateway.form.service.ServiceSupplierService;
import com.bulpros.eformsgateway.form.service.UserProfileService;
import com.bulpros.eformsgateway.form.utils.ValidationUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.*;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.InvalidCaseStatusClassifierException;
import com.bulpros.formio.exception.InvalidServiceStatusException;
import com.bulpros.formio.model.User;
import com.bulpros.formio.security.FormioUserService;
import com.bulpros.formio.utils.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bulpros.eformsgateway.form.utils.ValidationUtils.isNotPresent;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminServiceController {
    @Value("${keycloak.user.id.property}")
    private String keycloakUserIdProperty;

    private final ServiceService serviceService;
    private final FormioUserService userService;
    private final SubjectRegistrationService subjectRegistrationService;
    private final LegalPersonRegistrationService legalPersonRegistrationService;
    private final UserProfileService userProfileService;
    private final ServiceSupplierService serviceSupplierService;

    @Autowired
    public AdminServiceController(ServiceService serviceService, FormioUserService userService,
                                  SubjectRegistrationService subjectRegistrationService,
                                  LegalPersonRegistrationService legalPersonRegistrationService,
                                  UserProfileService userProfileService, ServiceSupplierService serviceSupplierService) {
        this.serviceService = serviceService;
        this.userService = userService;
        this.subjectRegistrationService = subjectRegistrationService;
        this.legalPersonRegistrationService = legalPersonRegistrationService;
        this.userProfileService = userProfileService;
        this.serviceSupplierService = serviceSupplierService;
    }

    @GetMapping(path = "/projects/{projectId}/eas", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getServiceSubmissionWithPagination(Authentication authentication,
                                                                         @PathVariable String projectId,
                                                                         AdminServiceFilter serviceFilter,
                                                                         @RequestParam(required = false) Long page,
                                                                         @RequestParam(required = false) Long size,
                                                                         @RequestParam(required = false) String sort) {
        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (serviceFilter.getServiceStatuses() != null && !serviceFilter.getServiceStatuses().isEmpty() &&
                !serviceFilter.getServiceStatuses().stream().allMatch(ValidationUtils::isServiceStatusValid)) {
            throw new InvalidServiceStatusException("There is at least one invalid service status: " + serviceFilter.getServiceStatuses());
        }
        if (isNotPresent(serviceFilter.getApplicant())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (userProfileService.hasUserRole(userProfile, serviceFilter.getApplicant(), AdditionalProfileRoleEnum.MetadataManager)) {
            ServiceSupplierDto serviceSupplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, serviceFilter.getApplicant());
            if (serviceSupplier == null) {
                return getEmptyPageResponse();
            }
            serviceFilter.setServiceSupplierId(serviceSupplier.getCode());
            Page<ResourceDto> response = serviceService.getServices(projectId, authentication, serviceFilter, page, size, sort);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping(path = "/projects/{projectId}/eas/identifiers", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ServiceIdAndNameDto>> getServiceSubmissionByCaseStatusClassifier(Authentication authentication,
                                                                                         AdminServiceFilter serviceFilter,
                                                                                         @PathVariable String projectId,
                                                                                         @RequestParam String classifier) {
        UserProfileDto userProfile = userProfileService.getUserProfileData(projectId, authentication);
        if (ValidationUtils.getValidCaseClassifiers(classifier).isEmpty()) {
            log.error("Invalid classifier.");
            throw new InvalidCaseStatusClassifierException("Invalid case status classifier: " + classifier + ".");
        }
        if (isNotPresent(serviceFilter.getApplicant())) {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (userProfileService.hasUserRole(userProfile, serviceFilter.getApplicant(), AdditionalProfileRoleEnum.ServiceManager)) {
            ServiceSupplierDto serviceSupplier = serviceSupplierService.getServiceSupplierByEik(projectId, authentication, serviceFilter.getApplicant());
            if (serviceSupplier == null) {
                return getEmptyListResponse();
            }
            serviceFilter.setServiceSupplierId(serviceSupplier.getCode());
            List<ServiceIdAndNameDto> response = serviceService.getServicesByCaseStatusClassifierAndName(projectId, authentication, serviceFilter, classifier);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Page<ResourceDto>> getEmptyPageResponse() {
        return ResponseEntity.ok(new Page<>(0L, 0L, List.of()));
    }

    private ResponseEntity<List<ServiceIdAndNameDto>> getEmptyListResponse() {
        return ResponseEntity.ok(List.of());
    }
}
