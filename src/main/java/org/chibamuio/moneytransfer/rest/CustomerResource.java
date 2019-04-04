package org.chibamuio.moneytransfer.rest;

import org.chibamuio.moneytransfer.services.CustomerService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("money-transfer")
@RequestScoped
public class CustomerResource {

    private CustomerService customerService;

    @Inject
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }


}
