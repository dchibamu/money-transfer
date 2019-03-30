package org.chibamuio.moneytransfer.dao;

import org.chibamuio.moneytransfer.domain.Customer;

public interface CustomerDao<T> extends GenericDao<Customer, Long> {
    boolean isNewCustomer(long nationalIdNumber);
}
