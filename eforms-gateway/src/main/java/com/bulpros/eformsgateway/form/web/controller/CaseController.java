package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.files.service.FilePermissionEvaluator;
import com.bulpros.eformsgateway.files.service.FileService;
import com.bulpros.eformsgateway.form.service.CaseService;
import com.bulpros.eformsgateway.form.utils.ValidationUtils;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.eformsgateway.security.service.UserService;
import com.bulpros.eformsgateway.user.model.User;
import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.exception.InvalidCaseStatusClassifierException;
import com.bulpros.formio.utils.Page;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class CaseController {
    private final CaseService caseService;
    private final UserService userService;
    private final FileService fileService;
    private final FilePermissionEvaluator filePermissionEvaluator;

    public CaseController(CaseService caseService, UserService userService, FileService fileService,
                          @Qualifier("user") FilePermissionEvaluator filePermissionEvaluator) {
        this.caseService = caseService;
        this.userService = userService;
        this.fileService = fileService;
        this.filePermissionEvaluator = filePermissionEvaluator;
    }

    @Timed(value = "eforms-gateway-get-case-submissions-case-status.time")
    @GetMapping(path = "/projects/{projectId}/cases", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<ResourceDto>> getCaseSubmissionsByCaseStatusClassifier(
            Authentication authentication,
            @PathVariable String projectId,
            CaseFilter caseFilter,
            @RequestParam String classifier,
            @RequestParam(required = false) Boolean assignedToMe,
            @RequestParam(required = false) Long page,
            @RequestParam(required = false) Long size,
            @RequestParam(required = false) String sort) {

        User user = userService.getUser(authentication);

        if (ValidationUtils.getValidCaseClassifiers(classifier).isEmpty()) {
            log.error("Invalid classifiers.");
            throw new InvalidCaseStatusClassifierException("Invalid case status classifier: " + classifier + ".");
        }

        caseFilter.setRequestor(user.getPersonIdentifier());
        var response = caseService.getCasesByUserIdAndStatusClassifier(
                projectId, authentication, classifier, caseFilter, page, size, sort);

        return ResponseEntity.ok(response);
    }

    @Timed(value = "eforms-gateway-get-case-submission-by-id.time")
    @GetMapping(path = "/projects/{projectId}/cases/{caseBusinessKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ResourceDto> getCaseSubmissionById(
            Authentication authentication, @PathVariable String projectId, @PathVariable String caseBusinessKey, CaseFilter caseFilter) {
        User user = userService.getUser(authentication);
        caseFilter.setRequestor(user.getPersonIdentifier());
        var response = caseService.getCaseByUserIdAndCaseBusinessKey(projectId, authentication, caseBusinessKey, caseFilter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}/cases/files")
    @ResponseBody
    public ResponseEntity<List<FileDto>> handleCaseFiles(Authentication authentication,
                                                         @PathVariable String projectId,
                                                         CaseFilter caseFilter) {

        FileFilter fileFilter = new FileFilter();
        fileFilter.setBusinessKey(caseFilter.getBusinessKey());
        if (filePermissionEvaluator.hasDownloadPermission(authentication, projectId, caseFilter, fileFilter)) {

            return ResponseEntity.ok().body(fileService.caseFiles(projectId, fileFilter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
