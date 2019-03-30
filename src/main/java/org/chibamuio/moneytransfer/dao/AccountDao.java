package org.chibamuio.moneytransfer.dao;

import org.chibamuio.moneytransfer.domain.Account;

import java.util.List;

public interface AccountDao<T> extends GenericDao<Account, Long> {
    List<Account> findByCustomerId(long nationalIdNumber);
    boolean accountExist(long accountNumber);
}
