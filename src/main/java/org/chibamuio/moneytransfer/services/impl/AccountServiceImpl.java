package org.chibamuio.moneytransfer.services.impl;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.domain.Transaction;
import org.chibamuio.moneytransfer.domain.TransactionType;
import org.chibamuio.moneytransfer.exceptions.AccountNumberNotFoundException;
import org.chibamuio.moneytransfer.exceptions.CloseNoneEmptyAccountException;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;
import org.chibamuio.moneytransfer.exceptions.InSufficientFundsException;
import org.chibamuio.moneytransfer.rest.dto.CustomerDto;
import org.chibamuio.moneytransfer.rest.dto.DepositReqDto;
import org.chibamuio.moneytransfer.rest.dto.TransferDto;
import org.chibamuio.moneytransfer.rest.dto.WithdrawalReqDto;
import org.chibamuio.moneytransfer.services.AccountService;
import org.chibamuio.moneytransfer.services.CustomerService;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private AccountDao<Account> accountDao;
    private CustomerDao<Customer> customerDao;
    private TransactionDao<Transaction> transactionDao;

    @Inject
    public AccountServiceImpl(AccountDao<Account> accountDao, CustomerDao<Customer> customerDao, TransactionDao<Transaction> transactionDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public Optional<Account> create(CustomerDto customerDto){
        Customer customer;
        if(customerDao.isNewCustomer(customerDto.getNationalIdNumber())){
            customer = Customer.getBuilder()
                    .withNationalIdNumber(customerDto.getNationalIdNumber())
                    .withFirstName(customerDto.getFirstName())
                    .withLastName(customerDto.getLastName())
                    .withCreatedAt()
                    .build();
            customerDao.persist(customer);
        }else {
            customer = customerDao.findOne(customerDto.getNationalIdNumber()).orElseThrow(
                    () -> new CustomerNotFoundException(customerDto.getNationalIdNumber())
            );
        }

        long accountNumber = generateAccountNumber();
        while(accountDao.accountExist(accountNumber)){
            accountNumber = generateAccountNumber();
        }
        Money openingBalance = Money.of(customerDto.getAmount(), Monetary.getCurrency(customerDto.getCurrency()));
        Account account = Account.getBuilder()
                .withCustomer(customer)
                .withAccountNumber(accountNumber)
                .withBalance(openingBalance)
                .build();
        accountDao.persist(account);

        Transaction transaction = Transaction.getBuilder()
                                    .withTransactionType(TransactionType.OPEN_ACCOUNT)
                                    .withCreatedAt(LocalDateTime.now())
                                    .withDestinationAccount(account)
                                    .withCurrency(openingBalance.getCurrency().getCurrencyCode())
                                    .withAmount(openingBalance.getNumberStripped())
                                    .withTransactionId(generateTransactionId())
                                    .build();
        transactionDao.persist(transaction);
        return Optional.of(account);
    }

    @Override
    public List<Account> getAll(long nationalIdNumber) throws CustomerNotFoundException{
        Customer customer = customerDao.findOne(nationalIdNumber).orElseThrow(
                () -> new CustomerNotFoundException(nationalIdNumber)
        );
        return accountDao.findByCustomerId(customer.getNationalIdNumber());
    }

    @Override
    public void deposit(DepositReqDto depositReqDto) throws AccountNumberNotFoundException{
        Account account = findAccountByAccNo(depositReqDto.getAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(depositReqDto.getAccountNumber())
        );
        Money depositAmount = Money.of(depositReqDto.getAmount(), depositReqDto.getCurrency());
        Transaction transaction = Transaction.getBuilder()
                .withTransactionId(generateTransactionId())
                .withTransactionType(TransactionType.DEPOSIT)
                .withAmount(depositAmount.getNumberStripped())
                .withCurrency(depositAmount.getCurrency().getCurrencyCode())
                .withDestinationAccount(account)
                .withCreatedAt(LocalDateTime.now())
                .build();
        transactionDao.persist(transaction);
        MonetaryAmount additionalAmt = Monetary.getDefaultAmountFactory()
                                                .setCurrency(depositAmount.getCurrency())
                                                .setNumber(depositAmount.getNumber())
                                                .create();
        account.setBalance(account.getBalance().add(additionalAmt));
        account.setLastModified(LocalDateTime.now());
    }

    @Override
    public void withdraw(WithdrawalReqDto withdrawalReqDto) throws AccountNumberNotFoundException, InSufficientFundsException{
        Account account = findAccountByAccNo(withdrawalReqDto.getAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(withdrawalReqDto.getAccountNumber())
        );
        Money withdrawalAmount = Money.of(withdrawalReqDto.getAmount(), withdrawalReqDto.getCurrency());

        if(account.getBalance().getNumberStripped().compareTo(withdrawalReqDto.getAmount()) < 0)
            throw new InSufficientFundsException(withdrawalAmount, withdrawalReqDto.getAccountNumber());

        Transaction transaction = Transaction.getBuilder()
                .withTransactionId(generateTransactionId())
                .withTransactionType(TransactionType.WITHDRAWAL)
                .withAmount(withdrawalAmount.getNumberStripped())
                .withCurrency(withdrawalAmount.getCurrency().getCurrencyCode())
                .withSourceAccount(account)
                .withCreatedAt(LocalDateTime.now())
                .build();
        transactionDao.persist(transaction);
        MonetaryAmount additionalAmt = Monetary.getDefaultAmountFactory()
                .setCurrency(withdrawalAmount.getCurrency())
                .setNumber(withdrawalAmount.getNumber())
                .create();
        account.setBalance(account.getBalance().subtract(additionalAmt));
        account.setLastModified(LocalDateTime.now());
    }

    @Override
    public void transfer(TransferDto transferDto) throws AccountNumberNotFoundException, InSufficientFundsException {
        Account sourceAccount = findAccountByAccNo(transferDto.getSourceAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(transferDto.getSourceAccountNumber())
        );
        Money transferAmount = Money.of(transferDto.getAmount(), transferDto.getCurrency());

        if(sourceAccount.getBalance().getNumberStripped().compareTo(transferDto.getAmount()) < 0)
            throw new InSufficientFundsException(transferAmount, transferDto.getSourceAccountNumber());

        Account targetAccount = findAccountByAccNo(transferDto.getTargetAccountNumber()).orElseThrow(
                () -> new AccountNumberNotFoundException(transferDto.getTargetAccountNumber())
        );
        Transaction transaction = Transaction.getBuilder()
                .withTransactionId(generateTransactionId())
                .withAmount(transferAmount.getNumberStripped())
                .withCurrency(transferAmount.getCurrency().getCurrencyCode())
                .withTransactionType(TransactionType.TRANSFER)
                .withSourceAccount(sourceAccount)
                .withDestinationAccount(targetAccount)
                .withCreatedAt(LocalDateTime.now())
                .build();
        MonetaryAmount additionalAmt = Monetary.getDefaultAmountFactory()
                .setCurrency(transferAmount.getCurrency())
                .setNumber(transferAmount.getNumber())
                .create();
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(additionalAmt));
        targetAccount.setBalance(targetAccount.getBalance().add(additionalAmt));
        sourceAccount.setLastModified(LocalDateTime.now());
        targetAccount.setLastModified(LocalDateTime.now());
        transactionDao.persist(transaction);
    }

    @Override
    public void close(long accountNumber) throws CloseNoneEmptyAccountException, AccountNumberNotFoundException {
        Account targetAccount = findAccountByAccNo(accountNumber).orElseThrow(
                () -> new AccountNumberNotFoundException(accountNumber)
        );
        MonetaryAmount zeroAmount = Monetary.getDefaultAmountFactory()
                .setCurrency(targetAccount.getBalance().getCurrency())
                .setNumber(BigDecimal.ZERO).create();
        if(targetAccount.getBalance().isEqualTo(zeroAmount)){
            throw new CloseNoneEmptyAccountException(targetAccount.getAccountNumber());
        }
        accountDao.delete(targetAccount.getAccountNumber());
    }

    @Override
    public Optional<Account> findAccountByAccNo(long accountNumber) {
        return accountDao.findOne(accountNumber);
    }

    @Override
    public Money balance(long accountNumber) throws AccountNumberNotFoundException {
        Account account = findAccountByAccNo(accountNumber).orElseThrow(
                () -> new AccountNumberNotFoundException(accountNumber)
        );
        return account.getBalance();
    }

    private long generateAccountNumber() {
        return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }

    private long generateTransactionId() {
        return Instant.now().toEpochMilli();
    }
}
