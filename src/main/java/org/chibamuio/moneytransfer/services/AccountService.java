package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.exceptions.AccountNumberNotFoundException;
import org.chibamuio.moneytransfer.exceptions.CloseNoneEmptyAccountException;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;
import org.chibamuio.moneytransfer.exceptions.InSufficientFundsException;
import org.chibamuio.moneytransfer.rest.dto.CustomerDto;
import org.chibamuio.moneytransfer.rest.dto.DepositReqDto;
import org.chibamuio.moneytransfer.rest.dto.TransferDto;
import org.chibamuio.moneytransfer.rest.dto.WithdrawalReqDto;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> create(CustomerDto customerDto);
    List<Account> getAll(long nationalIdNumber) throws CustomerNotFoundException;
    void deposit(DepositReqDto depositReqDto) throws AccountNumberNotFoundException;
    void withdraw(WithdrawalReqDto withdrawalReqDto) throws AccountNumberNotFoundException, InSufficientFundsException;
    void transfer(TransferDto transferDto) throws AccountNumberNotFoundException, InSufficientFundsException;
    void close(long accountNumber) throws CloseNoneEmptyAccountException, AccountNumberNotFoundException;
    Optional<Account> findAccountByAccNo(long accountNumber);
    Money balance(long accountNumber) throws AccountNumberNotFoundException;
}
