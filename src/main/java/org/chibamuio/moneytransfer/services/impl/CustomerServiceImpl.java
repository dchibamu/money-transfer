package org.chibamuio.moneytransfer.services.impl;

import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao<Customer> customerDao;
    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Inject
    public CustomerServiceImpl(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public Optional<Customer> findByNationalIdNumber(long nationalIdNumber) {
        return customerDao.findOne(nationalIdNumber);
    }
}
