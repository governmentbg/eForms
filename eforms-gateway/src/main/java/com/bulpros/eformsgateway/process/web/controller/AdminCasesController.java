package com.bulpros.eformsgateway.process.web.controller;

import com.bulpros.eformsgateway.process.service.ProcessService;
import com.bulpros.eformsgateway.process.web.dto.AdminCaseMessageRequestDto;
import com.bulpros.eformsgateway.process.web.dto.AdminCaseMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminCasesController {

    private final ProcessService processService;

    @PostMapping(value = "cases/{businessKey}/messages", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCaseMessageResponseDto> caseMessage(Authentication authentication,
                                                                   @PathVariable("businessKey") String businessKey,
                                                                   @RequestHeader("Dp-Miscinfo") String miscInfo,
                                                                   @Valid @RequestBody AdminCaseMessageRequestDto requestDto) {

        AdminCaseMessageResponseDto responseDto = processService.message(authentication, businessKey, miscInfo, requestDto);
        return new ResponseEntity<>(responseDto, responseDto.getStatus());

    }

}
