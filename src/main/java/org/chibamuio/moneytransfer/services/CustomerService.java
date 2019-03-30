package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findByNationalIdNumber(long nationalIdNumber) throws CustomerNotFoundException;
}
