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
public class AccountNumberNotFoundExceptionMapper implements ExceptionMapper<AccountNumberNotFoundException> {

    private static final Logger LOG = LoggerFactory.getLogger(AccountNumberNotFoundExceptionMapper.class);

    @Override
    public Response toResponse(AccountNumberNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
        errorMessage.setCode(AppConstants.NOT_FOUND_ACCOUNT);
        errorMessage.setMessage(exception.getMessage());
        LOG.error(errorMessage.toString(), exception);
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
