package org.chibamuio.moneytransfer.config;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.dao.impl.AccountDaoImpl;
import org.chibamuio.moneytransfer.dao.impl.CustomerDaoImpl;
import org.chibamuio.moneytransfer.dao.impl.TransactionDaoImpl;
import org.chibamuio.moneytransfer.exceptions.*;
import org.chibamuio.moneytransfer.services.AccountService;
import org.chibamuio.moneytransfer.services.CustomerService;
import org.chibamuio.moneytransfer.services.impl.AccountServiceImpl;
import org.chibamuio.moneytransfer.services.impl.CustomerServiceImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class JaxRsApplication extends ResourceConfig {

    public JaxRsApplication() {
        packages("org.chibamuio.moneytransfer")
        .property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true)
        .register(JacksonFeature.class)
        .register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(AccountServiceImpl.class).to(AccountService.class);
                bind(CustomerServiceImpl.class).to(CustomerService.class);
                bind(AccountDaoImpl.class).to(AccountDao.class);
                bind(CustomerDaoImpl.class).to(CustomerDao.class);
                bind(TransactionDaoImpl.class).to(TransactionDao.class);
                bind(CloseNoneEmptyAccountExceptionMapper.class);
                bind(AccountNumberNotFoundExceptionMapper.class);
                bind(CustomerNotFoundExceptionMapper.class);
                bind(InSufficientFundsExceptionMapper.class);
                bind(GenericExceptionMapper.class);
                bind(InputValidationExceptionMapper.class);

            }
        });
    }
}
