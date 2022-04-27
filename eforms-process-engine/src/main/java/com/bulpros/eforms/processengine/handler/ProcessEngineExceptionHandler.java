package com.bulpros.eforms.processengine.handler;

import com.bulpros.eforms.processengine.exeptions.ForbiddenTaskException;
import com.bulpros.eforms.processengine.exeptions.ProcessNotFoundException;
import com.bulpros.eforms.processengine.exeptions.TaskNotFoundException;
import com.bulpros.eforms.processengine.exeptions.VariableNotFoundException;
import com.bulpros.eforms.processengine.web.exception.EFormsProcessEngineException;
import com.bulpros.eforms.processengine.web.exception.ExceptionBody;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngineException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
@Slf4j
public class ProcessEngineExceptionHandler {

    @ExceptionHandler(value = {TaskNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleTaskNotFoundException(TaskNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ProcessNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleProcessNotFoundException(ProcessNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "PROCESS_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {VariableNotFoundException.class})
    public ResponseEntity<ExceptionBody> handleVariableNotFoundException(VariableNotFoundException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.NOT_FOUND, "VARIABLE_NOT_FOUND"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ForbiddenTaskException.class})
    public ResponseEntity<ExceptionBody> handleForbiddenResourceException(ForbiddenTaskException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(HttpStatus.FORBIDDEN, "FORBIDDEN_TASK"),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {ProcessEngineException.class, EFormsProcessEngineException.class})
    public ResponseEntity<ExceptionBody> handleProcessEngineException(EFormsProcessEngineException exception) {
        log.error(exception.getMessage());
        return toResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ExceptionBody> handleHttpError(HttpStatusCodeException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ExceptionBody(exception.getStatusCode(), exception.getMessage()),
                exception.getStatusCode());
    }

    protected ResponseEntity<ExceptionBody> toResponse(Exception exception, HttpStatus httpStatus) {
        ExceptionBody exceptionDto = fromException(exception, httpStatus);
        return new ResponseEntity<ExceptionBody>(exceptionDto, httpStatus);
    }

    protected ExceptionBody fromException(Throwable t, HttpStatus httpStatus) {
        if (t instanceof ProcessEngineException) {
            return fromException(t.getCause(), httpStatus);
        } else if (t instanceof EFormsProcessEngineException) {
            EFormsProcessEngineException e = (EFormsProcessEngineException) t;
            return new ExceptionBody(httpStatus, e.getMessage(), e.getData());
        }
        return new ExceptionBody(httpStatus, t.getMessage());
    }

}
