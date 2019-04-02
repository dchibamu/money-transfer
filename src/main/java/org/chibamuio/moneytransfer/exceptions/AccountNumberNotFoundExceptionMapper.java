package org.chibamuio.moneytransfer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNumberNotFoundExceptionMapper implements ExceptionMapper<AccountNumberNotFoundException> {
    private static final Logger LOG = LoggerFactory.getLogger(AccountNumberNotFoundException.class);


    @Override
    public Response toResponse(AccountNumberNotFoundException exception) {
        LOG.error(exception.getMessage(), exception);
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
