package org.chibamuio.moneytransfer.dao.impl;

import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Transaction;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionDaoImpl implements TransactionDao<Transaction> {

    private volatile static Map<Long,Transaction> transactionMap = new ConcurrentHashMap<>();

    @Override
    public void persist(Transaction transaction) {
        transactionMap.put(transaction.getTransactionId(), transaction);
    }

    @Override
    public Optional<Transaction> update(Long transactionId, Transaction transaction) {
        return Optional.ofNullable(transactionMap.put(transactionId, transaction));
    }

    @Override
    public Optional<Transaction> findOne(Long transactionId) {
        return Optional.ofNullable(transactionMap.get(transactionId));
    }

    @Override
    public void delete(Long transactionId) {
        transactionMap.remove(transactionId);
    }
}
