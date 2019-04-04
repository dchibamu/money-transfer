package org.chibamuio.moneytransfer.services.impl;

import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Transaction;
import org.chibamuio.moneytransfer.services.TransactionService;

import javax.inject.Inject;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {


    private TransactionDao<Transaction> transactionDao;

    @Inject
    public TransactionServiceImpl(TransactionDao<Transaction> transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public List<Transaction> findTransactionsByAccountNo(long accountNumber) {
        return null;
    }
}
