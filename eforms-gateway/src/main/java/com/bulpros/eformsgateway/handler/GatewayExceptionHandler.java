package com.bulpros.eformsgateway.handler;

import com.bulpros.eformsgateway.eformsintegrations.exception.*;
import com.bulpros.eformsgateway.form.exception.NotSatisfiedAssuranceLevel;
import com.bulpros.formio.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleResourceNotFoundException(ResourceNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserProfileNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleUserProfileNotFoundException(UserProfileNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "USER_PROFILE_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ForbiddenResourceException.class})
    public ResponseEntity<ExceptionBody> handleForbiddenResourceException(ForbiddenResourceException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "FORBIDDEN_RESOURCE"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {NotActiveException.class})
    public ResponseEntity<ExceptionBody> handleNotActiveException(NotActiveException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "SERVICE_NOT_ACTIVE"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalFormioArgumentException.class})
    public ResponseEntity<ExceptionBody> handleIllegalFormioArgumentException(IllegalFormioArgumentException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "ILLEGAL_FORMIO_ARGUMENT"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingUserPinException.class})
    public ResponseEntity<ExceptionBody> handleMissingUserPinException(MissingUserPinException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "MISSING_USER_PIN"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidCaseStatusClassifierException.class})
    public ResponseEntity<ExceptionBody> handleInvalidCaseStatusClassifierException(InvalidCaseStatusClassifierException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "INVALID_CASE_STATUS_CLASSIFIER"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotSatisfiedAssuranceLevel.class})
    public ResponseEntity<ExceptionBody> handleNotSatisfiedAssuranceLevel(NotSatisfiedAssuranceLevel exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "NOT_SATISFIED_ASSURANCE_LEVEL"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {InvalidServiceStatusException.class})
    public ResponseEntity<ExceptionBody> handleInvalidServiceStatusException(InvalidServiceStatusException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.BAD_REQUEST, "INVALID_SERVICE_STATUS"),
                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(value = {ServiceNotAvailableException.class})
    public ResponseEntity<ExceptionBody> handleServiceNotAvailableException(ServiceNotAvailableException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {CheckEDeliveryRegistrationException.class})
    public ResponseEntity<ExceptionBody> handleCheckEDeliveryRegistrationException(CheckEDeliveryRegistrationException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ExceptionBody> handleHttpError(HttpStatusCodeException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(exception.getStatusCode(), exception.getMessage()),
                exception.getStatusCode());
    }

    static class ExceptionBody {
        private final int status;
        private final String error;
        private final String message;

        public ExceptionBody(HttpStatus status, String message) {
            super();
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }
}
