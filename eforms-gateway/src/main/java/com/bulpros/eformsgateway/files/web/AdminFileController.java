package com.bulpros.eformsgateway.files.web;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.repository.model.FileFilter;
import com.bulpros.eformsgateway.files.service.FilePermissionEvaluator;
import com.bulpros.eformsgateway.files.service.FileService;
import com.bulpros.eformsgateway.form.web.controller.dto.AdminCaseFilter;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/admin")
@Slf4j
public class AdminFileController {


    private final FileService fileService;
    private final FilePermissionEvaluator filePermissionEvaluator;

    public AdminFileController(FileService fileService, @Qualifier("admin") FilePermissionEvaluator filePermissionEvaluator) {
        this.fileService = fileService;
        this.filePermissionEvaluator = filePermissionEvaluator;
    }

    @GetMapping("/project/{projectId}/file")
    @ResponseBody
    public ResponseEntity<Resource> handleFileDownload(Authentication authentication,
                                                       @PathVariable String projectId,
                                                       @Valid AdminCaseFilter caseFilter,
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

    @RequestMapping(value = "/project/{projectId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public ResponseEntity<FileDto> handleFileUpload(Authentication authentication,
                                                    @PathVariable String projectId,
                                                    @RequestPart FilePart file,
                                                    @Valid FileFilter fileFilter) {

        if (filePermissionEvaluator.hasPermission(authentication, fileFilter)) {

            return ResponseEntity.ok().body(
                    fileService.store(fileFilter.getFilename(), file,
                            Objects.requireNonNull(file.headers().getContentType()).toString(),
                            projectId, fileFilter));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }


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