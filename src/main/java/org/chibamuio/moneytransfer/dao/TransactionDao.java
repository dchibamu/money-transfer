package org.chibamuio.moneytransfer.dao;

import org.chibamuio.moneytransfer.domain.Transaction;

import java.util.List;

public interface TransactionDao<T> extends GenericDao<Transaction, Long> {
    List<Transaction> findByAccountNumber(long accountNumber);
}
