package com.bulpros.eformsgateway.form.web.controller;

import com.bulpros.eformsgateway.form.service.LanguagesService;
import com.bulpros.eformsgateway.form.service.LanguagesStatusEnum;
import com.bulpros.formio.dto.ResourceDto;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@Slf4j
@RequiredArgsConstructor
public class PublicLanguagesController {
    private final LanguagesService languagesService;

    @Timed(value = "eforms-gateway-get-public-languages.time")
    @GetMapping(path = "/projects/{projectId}/languages", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ResourceDto>> getPublicLanguages(@PathVariable String projectId) {

        List<ResourceDto> response = languagesService.getLanguagesByStatus(projectId, LanguagesStatusEnum.PUBLIC.status);

        return ResponseEntity.ok(response);
    }
}
