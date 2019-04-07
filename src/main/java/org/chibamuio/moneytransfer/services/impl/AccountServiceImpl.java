package org.chibamuio.moneytransfer.services.impl;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.domain.Transaction;
import org.chibamuio.moneytransfer.domain.TransactionType;
import org.chibamuio.moneytransfer.exceptions.*;
import org.chibamuio.moneytransfer.rest.dto.BalanceDTO;
import org.chibamuio.moneytransfer.rest.dto.CustomerDTO;
import org.chibamuio.moneytransfer.rest.dto.DepositWithdrawalReqDTO;
import org.chibamuio.moneytransfer.rest.dto.TransferDTO;
import org.chibamuio.moneytransfer.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class AccountServiceImpl implements AccountService {

    private AccountDao<Account> accountDao;
    private CustomerDao<Customer> customerDao;
    private TransactionDao<Transaction> transactionDao;
    private static Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static Random rand = new Random();

    @Inject
    public AccountServiceImpl(AccountDao accountDao, CustomerDao customerDao, TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public Optional<Account> create(CustomerDTO customerDto) throws BusinessException {
        Customer customer = null;
        if (customerDao.isNewCustomer(customerDto.getNationalIdNumber())) {
            customer = Customer.getBuilder()
                    .withNationalIdNumber(customerDto.getNationalIdNumber())
                    .withFirstName(customerDto.getFirstName())
                    .withLastName(customerDto.getLastName())
                    .build();
            customerDao.persist(customer);
        } else {
            customer = customerDao.findOne(customerDto.getNationalIdNumber()).orElseThrow(
                    () -> new CustomerNotFoundException(customerDto.getNationalIdNumber())
            );
        }

        Account account = Account.getBuilder()
                .withCustomer(customer)
                .withBalance(customerDto.getAmount())
                .withCurrency(customerDto.getCurrency())
                .build();
        accountDao.persist(account);

        Transaction transaction = Transaction.getBuilder()
                .withTransactionType(TransactionType.OPEN_ACCOUNT)
                .withDestinationAccount(account)
                .withAmount(customerDto.getAmount())
                .build();
        transactionDao.persist(transaction);
        return Optional.of(account);
    }

    @Override
    public List<Account> getAll(long nationalIdNumber) throws CustomerNotFoundException {
        Customer customer = customerDao.findOne(nationalIdNumber).orElseThrow(
                () -> new CustomerNotFoundException(nationalIdNumber)
        );
        return accountDao.findByCustomerId(customer.getNationalIdNumber());
    }

    @Override
    public void deposit(DepositWithdrawalReqDTO depositReqDto) throws AccountNumberNotFoundException {
        Account account = findAccountByAccNo(depositReqDto.getAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(depositReqDto.getAccountNumber())
        );

        account.getWriteLock().lock();
        try {
            Transaction transaction = Transaction.getBuilder()
                    .withTransactionType(TransactionType.DEPOSIT)
                    .withAmount(depositReqDto.getAmount())
                    .withDestinationAccount(account)
                    .build();
            transactionDao.persist(transaction);
            account.setBalance(account.getBalance().add(depositReqDto.getAmount()));
            account.setLastModified(LocalDateTime.now());
        }finally {
            account.getWriteLock().unlock();
        }
    }

    @Override
    public void withdraw(DepositWithdrawalReqDTO withdrawalReqDto) throws AccountNumberNotFoundException, InsufficientFundsException {
        Account account = findAccountByAccNo(withdrawalReqDto.getAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(withdrawalReqDto.getAccountNumber())
        );

        if (account.getBalance().compareTo(withdrawalReqDto.getAmount()) < 0)
            throw new InsufficientFundsException(withdrawalReqDto.getAmount(), account.getCurrency(), withdrawalReqDto.getAccountNumber());

        account.getWriteLock().lock();
        try {
            Transaction transaction = Transaction.getBuilder()
                    .withTransactionType(TransactionType.WITHDRAWAL)
                    .withAmount(withdrawalReqDto.getAmount())
                    .withSourceAccount(account)
                    .build();
            transactionDao.persist(transaction);
            account.setBalance(account.getBalance().subtract(withdrawalReqDto.getAmount()));
            account.setLastModified(LocalDateTime.now());
        }finally {
            account.getWriteLock().unlock();
        }
    }

    @Override
    public void transfer(TransferDTO transferDto) throws AccountNumberNotFoundException, InsufficientFundsException, SameAccountTransferException {
        if(transferDto.getSourceAccountNumber() == transferDto.getTargetAccountNumber()) {
            throw new SameAccountTransferException(transferDto.getSourceAccountNumber(), transferDto.getTargetAccountNumber());
        }
        Account sourceAccount = findAccountByAccNo(transferDto.getSourceAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(transferDto.getSourceAccountNumber())
        );

        if (sourceAccount.getBalance().compareTo(transferDto.getAmount()) < 0)
            throw new InsufficientFundsException(transferDto.getAmount(), sourceAccount.getCurrency(), transferDto.getSourceAccountNumber());

        Account targetAccount = findAccountByAccNo(transferDto.getTargetAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(transferDto.getTargetAccountNumber())
        );
        while (true) {
            if (sourceAccount.getWriteLock().tryLock()) {
                try {
                    if (targetAccount.getWriteLock().tryLock()) {
                        try {
                            Transaction transaction = Transaction.getBuilder()
                                    .withAmount(transferDto.getAmount())
                                    .withTransactionType(TransactionType.TRANSFER)
                                    .withSourceAccount(sourceAccount)
                                    .withDestinationAccount(targetAccount)
                                    .build();
                            sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferDto.getAmount()));
                            targetAccount.setBalance(targetAccount.getBalance().add(transferDto.getAmount()));
                            sourceAccount.setLastModified(LocalDateTime.now());
                            targetAccount.setLastModified(LocalDateTime.now());
                            transactionDao.persist(transaction);
                            return;
                        } finally {
                            targetAccount.getWriteLock().unlock();
                        }
                    }
                } finally {
                    sourceAccount.getWriteLock().unlock();
                }
            }
            int n = rand.nextInt(1000);
            int randomDelay = 1000 + n; // 1 second + random delay to prevent livelock
            try {
                NANOSECONDS.sleep(randomDelay);
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void close(long accountNumber) throws CloseNoneEmptyAccountException, AccountNumberNotFoundException {
        Account targetAccount = findAccountByAccNo(accountNumber).orElseThrow(
                () -> new AccountNumberNotFoundException(accountNumber)
        );

        if (targetAccount.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new CloseNoneEmptyAccountException(targetAccount.getAccountNumber());
        }
        targetAccount.getWriteLock().lock();
        try{
            accountDao.delete(targetAccount.getAccountNumber());
        }finally {
            targetAccount.getWriteLock().unlock();
        }

    }

    @Override
    public BalanceDTO balance(long accountNumber) throws AccountNumberNotFoundException {
        Account account = findAccountByAccNo(accountNumber).orElseThrow(
                () -> new AccountNumberNotFoundException(accountNumber)
        );
        account.getReadLock().lock();
        try{
            return new BalanceDTO(account.getBalance(), account.getCurrency());
        }finally {
            account.getReadLock().unlock();
        }
    }

    @Override
    public Optional<Account> findAccountByAccNo(long accountNumber) {
        return accountDao.findOne(accountNumber);
    }
}
