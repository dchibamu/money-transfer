package org.chibamuio.moneytransfer.dao;

import org.chibamuio.moneytransfer.domain.Transaction;

public interface TransactionDao<T> extends GenericDao<Transaction, Long> {
}
