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
public class CloseNoneEmptyAccountExceptionMapper implements ExceptionMapper<CloseNoneEmptyAccountException> {

    private static Logger LOG = LoggerFactory.getLogger(CloseNoneEmptyAccountExceptionMapper.class);

    @Override
    public Response toResponse(CloseNoneEmptyAccountException exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setStatus(Response.Status.NOT_FOUND.getStatusCode());
        errorMessage.setCode(AppConstants.NONE_EMPTY_ACCOUNT);
        errorMessage.setMessage(exception.getMessage());
        LOG.error(errorMessage.toString(), exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
