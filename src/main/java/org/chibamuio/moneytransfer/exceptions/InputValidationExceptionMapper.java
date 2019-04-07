package org.chibamuio.moneytransfer.exceptions;

import org.chibamuio.moneytransfer.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<InputValidationException> {
    private static Logger LOG = LoggerFactory.getLogger(InputValidationExceptionMapper.class);

    @Override
    public Response toResponse(InputValidationException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        errorMessage.setCode(AppConstants.INVALID_INPUT_DATA);
        errorMessage.setMessage(exception.getMessage());
        LOG.error(errorMessage.toString(), exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getErrors())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
