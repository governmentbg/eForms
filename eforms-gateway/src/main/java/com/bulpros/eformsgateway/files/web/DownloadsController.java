package com.bulpros.eformsgateway.files.web;

import com.bulpros.eformsgateway.files.repository.model.FileDto;
import com.bulpros.eformsgateway.files.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DownloadsController {

    private final FileService fileService;

    @GetMapping("/project/{projectId}/downloads/{filePath}")
    @ResponseBody
    public ResponseEntity<Resource> handleDownload(Authentication authentication,
                                                   @PathVariable String projectId,
                                                   @PathVariable String filePath) {

        FileDto file = fileService.load(projectId, filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, file.getContentType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getResource());
    }

}
