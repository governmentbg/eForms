package com.bulpros.eformsgateway.files.web;

import com.bulpros.eformsgateway.exception.SecurityViolationException;
import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.files.service.FilePermissionEvaluator;
import com.bulpros.eformsgateway.files.service.FileService;
import com.bulpros.eformsgateway.files.service.MalwareScanService;
import com.bulpros.eformsgateway.form.web.controller.dto.CaseFilter;
import com.bulpros.eformsgateway.process.repository.utils.ProcessConstants;
import com.bulpros.eformsgateway.process.service.ProcessService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserFileController {


    private final FileService fileService;
    private final FilePermissionEvaluator filePermissionEvaluator;
    private final ProcessService processService;
    private final MalwareScanService malwareScanService;

    public UserFileController(FileService fileService, @Qualifier("user") FilePermissionEvaluator filePermissionEvaluator, ProcessService processService, MalwareScanService malwareScanService) {
        this.fileService = fileService;
        this.filePermissionEvaluator = filePermissionEvaluator;
        this.processService = processService;
        this.malwareScanService = malwareScanService;
    }


    @Timed(value = "eforms-gateway-get-file-list.time")
    @GetMapping("/projects/{projectId}/edelivery-files-package/process/{process-id}")
    @ResponseBody
    public ResponseEntity<JSONObject> handleGetEdeliveryFilesPackageList(Authentication authentication,
                                                                         @PathVariable("projectId") String projectId,
                                                                         @PathVariable("process-id") String processInstanceId,
                                                                         CaseFilter caseFilter) {
        FileFilter fileFilter = new FileFilter();
        fileFilter.setBusinessKey(caseFilter.getBusinessKey());
        if (filePermissionEvaluator.hasDownloadPermission(authentication, projectId, caseFilter, fileFilter)) {
            var processContext = processService.getProcessVariableAsJsonObject(authentication,
                    processInstanceId, ProcessConstants.CONTEXT);
            Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
            String arId = JsonPath.using(pathConfiguration).parse(processContext).read("$.value.service.data.arId");

            var eDeliveryFilesPackageVariable = ProcessConstants.EDELIVERY_FILES_PACKAGE + arId +
                    ProcessConstants.FORMA_REQUEST_SUFFIX;

            return ResponseEntity.ok().body(processService.getProcessVariableAsJsonObject(authentication,
                    processInstanceId, eDeliveryFilesPackageVariable));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Timed(value = "eforms-gateway-file-download.time")
    @GetMapping("/project/{projectId}/file")
    @ResponseBody
    public ResponseEntity<Resource> handleFileDownload(Authentication authentication,
                                                       @PathVariable String projectId,
                                                       CaseFilter caseFilter,
                                                       @Valid FileFilter fileFilter) {

        if (filePermissionEvaluator.hasDownloadPermission(authentication, projectId, caseFilter, fileFilter)) {

            FileDto file = fileService.load(projectId, fileFilter);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getResource());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Timed(value = "eforms-gateway-file-upload.time")
    @RequestMapping(value = "/project/{projectId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public ResponseEntity<FileDto> handleFileUpload(Authentication authentication,
                                                    @PathVariable String projectId,
                                                    @RequestPart FilePart file,
                                                    @Valid FileFilter fileFilter) {

        if(!malwareScanService.scanForMalware(file)){
            throw new SecurityViolationException("ERROR.GATEWAY.MALWARESCAN.THREAD.FOUND");
        }

        if (filePermissionEvaluator.hasPermission(authentication, fileFilter)) {

            return ResponseEntity.ok().body(
                    fileService.store(fileFilter.getFilename(), file,
                            Objects.requireNonNull(file.headers().getContentType()).toString(),
                            projectId, fileFilter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


    @Timed(value = "eforms-gateway-delete-file.time")
    @DeleteMapping("/project/{projectId}/file")
    @ResponseBody
    public ResponseEntity handleFileDelete(Authentication authentication,
                                           @PathVariable String projectId,
                                           @Valid FileFilter fileFilter) {


        if (filePermissionEvaluator.hasPermission(authentication, fileFilter)) {
            fileService.delete(projectId, fileFilter);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


}