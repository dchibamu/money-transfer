package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.domain.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> findTransactionsByAccountNo(long accountNumber);
}
