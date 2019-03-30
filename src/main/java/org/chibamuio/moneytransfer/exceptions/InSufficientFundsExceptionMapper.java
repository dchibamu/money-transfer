package org.chibamuio.moneytransfer.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InSufficientFundsExceptionMapper implements ExceptionMapper<InSufficientFundsException> {

    @Override
    public Response toResponse(InSufficientFundsException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
