package org.chibamuio.moneytransfer.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.Response.Status.OK;

@Path("transfer")
public class TransactionResource {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("source") long source, @PathParam("target") long target, @PathParam("currency") String currency, @PathParam("amount") BigDecimal amount){
        return Response.status(OK).build();
    }

}
