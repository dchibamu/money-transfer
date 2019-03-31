package org.chibamuio.moneytransfer.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CloseNoneEmptyAccountExceptionMapper implements ExceptionMapper<CloseNoneEmptyAccountException> {

    @Override
    public Response toResponse(CloseNoneEmptyAccountException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
