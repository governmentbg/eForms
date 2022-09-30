package com.bulpros.eforms.processengine.web.controller;

import com.bulpros.eforms.processengine.camunda.service.ExpressionEvaluationService;
import com.bulpros.eforms.processengine.web.dto.AdminCaseMessageRequestDto;
import com.bulpros.eforms.processengine.web.dto.AdminCaseMessageResponseDto;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping({"/eforms-rest/admin"})
@AllArgsConstructor
public class AdminCaseMessageController {

    private final RuntimeService runtimeService;
    private final ExpressionEvaluationService expressionEvaluationService;

    @PostMapping(value = "case/message", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminCaseMessageResponseDto> paymentRequestCallback(Authentication authentication,
                                                                              @Valid @RequestBody AdminCaseMessageRequestDto request) {
        AdminCaseMessageResponseDto response = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(request.getBusinessKey())
                .active()
                .singleResult();
        if (processInstance == null) {
            response = new AdminCaseMessageResponseDto("No active case matching businessKey !",
                    HttpStatus.BAD_REQUEST);
        } else {
            String providerOID = expressionEvaluationService.evaluateString("#{context.serviceSupplier.data.providerOID}",
                    processInstance.getId());
            if (providerOID != null && providerOID.equals(request.getProviderOID())) {
                Execution execution = runtimeService.createExecutionQuery()
                        .messageEventSubscriptionName(request.getMessageName())
                        .processInstanceId(processInstance.getId())
                        .singleResult();

                if (execution != null) {
                    runtimeService.messageEventReceived(request.getMessageName(), execution.getId(), request.getProcessVariables());
                    response = new AdminCaseMessageResponseDto("Success !", HttpStatus.OK);
                } else {
                    response = new AdminCaseMessageResponseDto("No configured message event with this name !",
                            HttpStatus.BAD_REQUEST);
                }
            } else {
                response = new AdminCaseMessageResponseDto("Supplier with this OID don't have access to this case !",
                        HttpStatus.UNAUTHORIZED);
            }
        }
        return ResponseEntity.ok(response);
    }
}
