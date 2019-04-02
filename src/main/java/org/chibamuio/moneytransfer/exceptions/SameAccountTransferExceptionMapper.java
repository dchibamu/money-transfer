package org.chibamuio.moneytransfer.exceptions;

import org.chibamuio.moneytransfer.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.PrintWriter;
import java.io.StringWriter;

@Provider
public class SameAccountTransferExceptionMapper implements ExceptionMapper<SameAccountTransferException> {

    private static final Logger LOG = LoggerFactory.getLogger(SameAccountTransferException.class);

    @Override
    public Response toResponse(SameAccountTransferException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        errorMessage.setCode(AppConstants.GENERAL_ERROR_CODE);
        errorMessage.setMessage(exception.getMessage());
        StringWriter errorStackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(errorStackTrace));
        errorMessage.setStackTrace(errorStackTrace.toString());
        LOG.error(errorMessage.toString(), exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
