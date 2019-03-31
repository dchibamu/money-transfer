package org.chibamuio.moneytransfer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<InputValidationException> {
    private static Logger LOG = LoggerFactory.getLogger(InputValidationException.class);

    @Override
    public Response toResponse(InputValidationException exception) {
        LOG.error(exception.getErrors().toString(), exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getErrors())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
