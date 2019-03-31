package org.chibamuio.moneytransfer.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNumberNotFoundExceptionMapper implements ExceptionMapper<AccountNumberNotFoundException> {
    @Override
    public Response toResponse(AccountNumberNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
