package org.chibamuio.moneytransfer.exceptions;

import org.chibamuio.moneytransfer.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger LOG = LoggerFactory.getLogger(GenericExceptionMapper.class);
    @Override
    public Response toResponse(Throwable exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        if(exception instanceof WebApplicationException) {
            errorMessage.setStatus(((WebApplicationException)exception).getResponse().getStatus());
        } else {
            errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
        errorMessage.setCode(AppConstants.GENERAL_ERROR_CODE);
        errorMessage.setMessage(exception.getMessage());
        StringWriter errorStackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorStackTrace));
        errorMessage.setStackTrace(errorStackTrace.toString());
        LOG.error(errorMessage.toString(), exception);
        return Response.status(errorMessage.getStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
