package com.bulpros.eforms.processengine.web.exception;

import javax.annotation.Priority;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.rest.dto.ExceptionDto;
import org.camunda.bpm.engine.rest.exception.ExceptionHandlerHelper;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.camunda.bpm.engine.rest.exception.RestExceptionHandler;
import org.springframework.http.HttpStatus;

@Provider
@Priority(value = 0)
public class EFormsExceptionMapper extends RestExceptionHandler implements ExceptionMapper<RestException> {

    @Override
    public Response toResponse(RestException exception) {
        ExceptionBody exceptionDto = fromException(exception);

        return Response
            .status(exceptionDto.getStatus())
            .entity(exceptionDto)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .build();
    }

    public ExceptionBody fromException(Throwable t) {

        Response.Status responseStatus = getStatus(t);
        HttpStatus httpStatus = HttpStatus.valueOf(responseStatus.getStatusCode());

        if (t instanceof RestException) {
            if(t.getCause() != null) {
                return fromException(t.getCause());
            }
        } else if (t instanceof ProcessEngineException) {
            if(t.getCause() != null) {
                return fromException(t.getCause());
            }
        } else if (t instanceof EFormsProcessEngineException) {
            EFormsProcessEngineException e = (EFormsProcessEngineException) t;
            return new ExceptionBody(httpStatus, e.getFullCode(), e.getData());
        }
        ExceptionDto dto = ExceptionHandlerHelper.getInstance().fromException(t);
        return new ExceptionBody(httpStatus, "ERROR.PROCESS-ENGINE", dto.getMessage());
    }

    public Response.Status getStatus(Throwable t) {
        return ExceptionHandlerHelper.getInstance().getStatus(t);
    }
}
