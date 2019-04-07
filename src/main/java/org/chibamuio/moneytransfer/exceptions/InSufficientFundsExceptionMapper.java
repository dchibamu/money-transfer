package org.chibamuio.moneytransfer.exceptions;

import org.chibamuio.moneytransfer.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InSufficientFundsExceptionMapper implements ExceptionMapper<InsufficientFundsException> {

    private static final Logger LOG = LoggerFactory.getLogger(InSufficientFundsExceptionMapper.class);

    @Override
    public Response toResponse(InsufficientFundsException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Response.Status.BAD_REQUEST.getStatusCode());
        errorMessage.setCode(AppConstants.INSUFFICIENT_FUNDS);
        errorMessage.setMessage(exception.getMessage());
        LOG.error(errorMessage.toString(), exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
