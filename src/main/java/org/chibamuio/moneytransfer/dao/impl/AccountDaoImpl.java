package org.chibamuio.moneytransfer.dao.impl;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.domain.Account;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AccountDaoImpl implements AccountDao<Account> {

    private volatile static Map<Long, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public boolean accountExist(long accountNumber){
        return accounts.containsKey(accountNumber);
    }

    @Override
    public void persist(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public Optional<Account> update(Account entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> findOne(Long accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    @Override
    public void delete(Long accountNumber) {
        accounts.remove(accountNumber);
    }

    @Override
    public List<Account> findByCustomerId(long nationalIdNumber) {
        return accounts.values().stream().filter(acc -> acc.getCustomer().getNationalIdNumber() == nationalIdNumber).collect(Collectors.toList());
    }
}
