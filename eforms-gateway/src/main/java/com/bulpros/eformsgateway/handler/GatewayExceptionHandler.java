package com.bulpros.eformsgateway.handler;

import com.bulpros.eformsgateway.eformsintegrations.exception.*;
import com.bulpros.eformsgateway.exception.ExceptionBody;
import com.bulpros.eformsgateway.exception.SecurityViolationException;
import com.bulpros.eformsgateway.files.exception.FileMalwareScanFailedException;
import com.bulpros.eformsgateway.form.exception.*;
import com.bulpros.formio.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

    @ExceptionHandler(value = {FormioClientException.class})
    public ResponseEntity<ExceptionBody> handleFormioClientException(FormioClientException exception) {
        log.error(exception.getMessage(), exception);
        if(exception.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR.GATEWAY.FORMIO.UNAVAILABLE",
                    exception.getData()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<>(new ExceptionBody(exception.getStatus(), "ERROR.GATEWAY.FORMIO.COMMUNICATION",
                    exception.getData()),
                    exception.getStatus());
        }
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "ERROR.GATEWAY.RESOURCE_NOT_FOUND",
                exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {JsonProcessingException.class})
    public ResponseEntity<ExceptionBody> handleJsonProcessingException(JsonProcessingException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR.GATEWAY.JSON_OBJECT_PROCESSING_EXCEPTION", exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserProfileNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleUserProfileNotFoundException(UserProfileNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "ERROR.GATEWAY.USER_PROFILE_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ForbiddenResourceException.class})
    public ResponseEntity<ExceptionBody> handleForbiddenResourceException(ForbiddenResourceException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "ERROR.GATEWAY.FORBIDDEN_RESOURCE"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {NotActiveException.class})
    public ResponseEntity<ExceptionBody> handleNotActiveException(NotActiveException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.SERVICE_NOT_ACTIVE"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalFormioArgumentException.class})
    public ResponseEntity<ExceptionBody> handleIllegalFormioArgumentException(IllegalFormioArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.ILLEGAL_FORMIO_ARGUMENT"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingUserPinException.class})
    public ResponseEntity<ExceptionBody> handleMissingUserPinException(MissingUserPinException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.MISSING_USER_PIN"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidCaseStatusClassifierException.class})
    public ResponseEntity<ExceptionBody> handleInvalidCaseStatusClassifierException(InvalidCaseStatusClassifierException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.INVALID_CASE_STATUS_CLASSIFIER"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotSatisfiedAssuranceLevel.class})
    public ResponseEntity<ExceptionBody> handleNotSatisfiedAssuranceLevel(NotSatisfiedAssuranceLevel exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "ERROR.GATEWAY.NOT_SATISFIED_ASSURANCE_LEVEL"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {NotAllowedProfileType.class})
    public ResponseEntity<ExceptionBody> handleNotAllowedProfileType(NotAllowedProfileType exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "ERROR.GATEWAY.NOT_ALLOWED_PROFILE_TYPE",
                exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {MetadataUpdateFailed.class})
    public ResponseEntity<ExceptionBody> handleMetadataUpdateFailed(MetadataUpdateFailed exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.METADATA_UPDATE_FAILED",
                exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ServiceNotAvailableException.class})
    public ResponseEntity<ExceptionBody> handleServiceNotAvailableException(ServiceNotAvailableException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.getFullCode(),
                exception.getData()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {CheckEDeliveryRegistrationException.class})
    public ResponseEntity<ExceptionBody> handleCheckEDeliveryRegistrationException(CheckEDeliveryRegistrationException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ExceptionBody> handleHttpError(HttpStatusCodeException exception) {
        log.error(exception.getMessage(), exception);
        ExceptionBody exceptionBody = extractExceptionBody(exception.getResponseBodyAsString());
        return new ResponseEntity<>(Objects.requireNonNullElseGet(exceptionBody,
                () -> new ExceptionBody(exception.getStatusCode(), exception.getResponseBodyAsString())),
                exception.getStatusCode());
    }

    @ExceptionHandler(value = {InvalidPaymentStatusMessageException.class})
    public ResponseEntity<ExceptionBody> handleInvalidPaymentStatusException(InvalidPaymentStatusMessageException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "INVALID_PAYMENT_STATUS_MESSAGE"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {FileMalwareScanFailedException.class})
    public ResponseEntity<ExceptionBody> handleFileMalwareScanFailedException(FileMalwareScanFailedException exception) {
        log.error(exception.getMessage(), exception);
        var status = exception.getStatus();
        if(status.equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            return new ResponseEntity<>(new ExceptionBody(status, "ERROR.GATEWAY.MALWARESCAN.UNAVAILABLE", exception.getMessage()),
                exception.getStatus());
        }
        return new ResponseEntity<>(new ExceptionBody(status, "ERROR.GATEWAY.MALWARESCAN.COMMUNICATION", exception.getMessage()),
                exception.getStatus());
    }

    @ExceptionHandler(value = {SecurityViolationException.class})
    public ResponseEntity<ExceptionBody> handleFileSecurityViolationException(SecurityViolationException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ETranslationException.class})
    public ResponseEntity<ExceptionBody> handleETranslationException(ETranslationException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR.GATEWAY.ETRANSLATION",
                exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {TranslationFailed.class})
    public ResponseEntity<ExceptionBody> handleTranslationUpdateFailed(TranslationFailed exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR.GATEWAY.TRANSLATION",
                exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidLanguageStatusChangeException.class})
    public ResponseEntity<ExceptionBody> handleInvalidLanguageStatusChangeException(InvalidLanguageStatusChangeException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ERROR.GATEWAY.INVALID_LANGUAGE_STATUS_CHANGE",
                exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    public ExceptionBody extractExceptionBody(String exception) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(exception, ExceptionBody.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
