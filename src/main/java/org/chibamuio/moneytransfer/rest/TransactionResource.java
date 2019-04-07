package org.chibamuio.moneytransfer.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.Response.Status.OK;

@Path("transactions")
public class TransactionResource {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getStatement(){
        return Response.status(OK).build();
    }
}
