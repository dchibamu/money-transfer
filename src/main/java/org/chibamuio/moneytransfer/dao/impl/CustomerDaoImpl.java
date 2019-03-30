package org.chibamuio.moneytransfer.dao.impl;

import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CustomerDaoImpl implements CustomerDao<Customer> {

    private volatile static Map<Long, Customer > customers = new ConcurrentHashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);
    @Override
    public void persist(Customer customer) {
        customers.put(customer.getNationalIdNumber(), customer);
    }

    @Override
    public Optional<Customer> update(Customer entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findOne(Long nationalIdNumber) {
        return Optional.ofNullable(customers.get(nationalIdNumber));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public boolean isNewCustomer(long nationalIdNumber){
        return !customers.containsKey(nationalIdNumber);
    }
}
