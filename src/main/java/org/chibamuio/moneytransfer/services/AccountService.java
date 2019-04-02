package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.exceptions.*;
import org.chibamuio.moneytransfer.rest.dto.BalanceDTO;
import org.chibamuio.moneytransfer.rest.dto.CustomerDTO;
import org.chibamuio.moneytransfer.rest.dto.DepositWithdrawalReqDTO;
import org.chibamuio.moneytransfer.rest.dto.TransferDTO;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> create(CustomerDTO customerDto) throws BusinessException;
    List<Account> getAll(long nationalIdNumber) throws CustomerNotFoundException;
    void deposit(DepositWithdrawalReqDTO depositReqDto) throws AccountNumberNotFoundException;
    void withdraw(DepositWithdrawalReqDTO withdrawalReqDto) throws AccountNumberNotFoundException, InsufficientFundsException;
    void transfer(TransferDTO transferDto) throws AccountNumberNotFoundException, InsufficientFundsException, SameAccountTransferException;
    void close(long accountNumber) throws CloseNoneEmptyAccountException, AccountNumberNotFoundException;
    Optional<Account> findAccountByAccNo(long accountNumber);
    BalanceDTO balance(long accountNumber) throws AccountNumberNotFoundException;
}
