package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.domain.Transaction;
import org.chibamuio.moneytransfer.exceptions.*;
import org.chibamuio.moneytransfer.rest.dto.BalanceDTO;
import org.chibamuio.moneytransfer.rest.dto.CustomerDTO;
import org.chibamuio.moneytransfer.rest.dto.DepositWithdrawalReqDTO;
import org.chibamuio.moneytransfer.rest.dto.TransferDTO;
import org.chibamuio.moneytransfer.services.impl.AccountServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private CustomerDao<Customer> customerDao;

    @Mock
    private AccountDao<Account> accountDao;

    @Mock
    private TransactionDao<Transaction> transactionDao;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @InjectMocks
    private AccountServiceImpl accountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createAccountTest() throws BusinessException {
        CustomerDTO customerDto = CustomerDTO.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(customerDao.isNewCustomer(anyLong())).thenReturn(true);
        Optional<Account> account = accountService.create(customerDto);
        assertNotNull(account);
        verify(customerDao, times(1)).isNewCustomer(anyLong());
        verify(accountDao, times(1)).persist(any(Account.class));
        verify(transactionDao, times(1)).persist(any(Transaction.class));
    }

    @Test
    public void createAccountWhenCustomerIsNotNewTest() throws BusinessException {
        CustomerDTO customerDto = CustomerDTO.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(customerDao.isNewCustomer(anyLong())).thenReturn(false);
        when(customerDao.findOne(anyLong())).thenReturn(Optional.of(Customer.getBuilder().build()));
        Optional<Account> account = accountService.create(customerDto);
        assertNotNull(account);
        verify(customerDao, times(1)).isNewCustomer(anyLong());
        verify(accountDao, times(1)).persist(any(Account.class));
        verify(transactionDao, times(1)).persist(any(Transaction.class));
    }

    @Test
    public void getAllAccountsOnCustomerTest() throws BusinessException{
        when(customerDao.findOne(anyLong())).thenReturn(Optional.of(Customer.getBuilder().build()));
        when(accountDao.findByCustomerId(anyLong())).thenReturn(createMockAccounts());
        List<Account> accountList = accountService.getAll(9008047193475L);
        assertThat(accountList, is(not(empty())));
        assertThat(accountList, hasSize(equalTo(2)));
    }

    @Test
    public void getAccountsOnCustomerThrowException() throws BusinessException {
        when(customerDao.findOne(anyLong())).thenReturn(Optional.of(Customer.getBuilder().build()));
        when(accountDao.findByCustomerId(anyLong())).thenThrow(CustomerNotFoundException.class);
        exceptionRule.expect(CustomerNotFoundException.class);
        exceptionRule.expectMessage("Customer with national identity number 0 cannot be found.");
        List<Account> accountList = accountService.getAll(9008047193475L);
        assertThat(accountList, is(empty()));
    }

    @Test
    public void depositSuccessfulTest() throws BusinessException{
        DepositWithdrawalReqDTO depositReqDTO = new DepositWithdrawalReqDTO(9008047193475L, BigDecimal.valueOf(100));
        Account targetAcc = createMockAccount();
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.of(targetAcc));

        BalanceDTO balanceDTO = accountService.balance(9008047193475L);
        assertEquals(targetAcc.getBalance(), balanceDTO.getAmount());
        accountService.deposit(depositReqDTO);
        assertEquals(targetAcc.getBalance(), balanceDTO.getAmount().add(depositReqDTO.getAmount()));
    }

    @Test
    public void withdrawalSuccessfulTest() throws BusinessException{
        DepositWithdrawalReqDTO withdrawalReqDTO = new DepositWithdrawalReqDTO(9008047193475L, BigDecimal.valueOf(100.50));
        Account sourceAcc = createMockAccountWithdraw();
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.of(sourceAcc));
        BalanceDTO balanceDTO = accountService.balance(9008047193475L);
        assertEquals(sourceAcc.getBalance(), balanceDTO.getAmount());
        accountService.withdraw(withdrawalReqDTO);
        assertEquals(sourceAcc.getBalance(), balanceDTO.getAmount().subtract(withdrawalReqDTO.getAmount()));
    }

    @Test
    public void withdrawalThrowsInsufficientFundsExceptionTest() throws BusinessException{
        DepositWithdrawalReqDTO withdrawalReqDTO = new DepositWithdrawalReqDTO(9008047193475L, BigDecimal.valueOf(5000));
        Account sourceAcc = createMockAccountWithdraw();
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.of(sourceAcc));
        exceptionRule.expect(InsufficientFundsException.class);
        exceptionRule.expectMessage("Cannot withdraw USD5000.00. Insufficient funds in account 9008047193475");
        accountService.withdraw(withdrawalReqDTO);
    }

    @Test
    public void withdrawalThrowsAccountNotFoundExceptionTest() throws BusinessException{
        DepositWithdrawalReqDTO withdrawalReqDTO = new DepositWithdrawalReqDTO(1111111111111L, BigDecimal.valueOf(5000));
        exceptionRule.expect(AccountNumberNotFoundException.class);
        exceptionRule.expectMessage(String.format("Account number %d cannot be found.", 1111111111111L));
        accountService.withdraw(withdrawalReqDTO);
    }

    @Test
    public void successfullyTransferFundsTest() throws BusinessException {
        TransferDTO transferDTO = new TransferDTO(1554292743856L, 1554292860710L, BigDecimal.valueOf(450));
        Account sourceAccount = createMockSourceAccountTransfer();
        Account targetAccount = createMockTargetAccountTransfer();
        when(accountService.findAccountByAccNo(transferDTO.getSourceAccountNumber())).thenReturn(Optional.of(sourceAccount));
        when(accountService.findAccountByAccNo(transferDTO.getTargetAccountNumber())).thenReturn(Optional.of(targetAccount));
        BalanceDTO sourceBalanceBefore = accountService.balance(transferDTO.getSourceAccountNumber());
        BalanceDTO targetBalanceBefore = accountService.balance(transferDTO.getTargetAccountNumber());
        accountService.transfer(transferDTO);
        BalanceDTO sourceBalanceAfter = accountService.balance(transferDTO.getSourceAccountNumber());
        BalanceDTO targetBalanceAfter = accountService.balance(transferDTO.getTargetAccountNumber());
        assertEquals(sourceBalanceAfter.getAmount(), sourceBalanceBefore.getAmount().subtract(transferDTO.getAmount()));
        assertEquals(targetBalanceAfter.getAmount(), targetBalanceBefore.getAmount().add(transferDTO.getAmount()));
        verify(transactionDao, times(1)).persist(any(Transaction.class));
    }

    @Test
    public void failTransferOnSameAccountTransferException()throws BusinessException{
        TransferDTO transferDTO = new TransferDTO(1554292743856L, 1554292743856L, BigDecimal.valueOf(450));
        exceptionRule.expect(SameAccountTransferException.class);
        exceptionRule.expectMessage(String.format("Source account (%s) and destination account (%s) are the same", transferDTO.getSourceAccountNumber(), transferDTO.getTargetAccountNumber()));
        accountService.transfer(transferDTO);
    }

    @Test
    public void failTransferOnSourceAccountNumberNotFoundException() throws BusinessException{
        TransferDTO transferDTO = new TransferDTO(1554292743856L, 1554292860710L, BigDecimal.valueOf(450));
        when(accountService.findAccountByAccNo(transferDTO.getSourceAccountNumber())).thenReturn(Optional.empty());
        exceptionRule.expect(AccountNumberNotFoundException.class);
        exceptionRule.expectMessage(String.format("Account number %d cannot be found.", transferDTO.getSourceAccountNumber()));
        accountService.transfer(transferDTO);
    }

    @Test
    public void failTransferOnSourceAccountInsufficientFundsException() throws BusinessException{
        TransferDTO transferDTO = new TransferDTO(1554292743856L, 1554292860710L, BigDecimal.valueOf(100000));
        Account sourceAccount = createMockSourceAccountTransfer();
        when(accountService.findAccountByAccNo(transferDTO.getSourceAccountNumber())).thenReturn(Optional.of(sourceAccount));
        exceptionRule.expect(InsufficientFundsException.class);
        exceptionRule.expectMessage(String.format("Cannot withdraw %s%s. Insufficient funds in account %d", sourceAccount.getCurrency(), transferDTO.getAmount().setScale(2, RoundingMode.CEILING), transferDTO.getSourceAccountNumber()));
        accountService.transfer(transferDTO);
    }

    @Test
    public void successfullyCloseAccount() throws BusinessException {
        Account targetAccount = createMockCloseAccount(BigDecimal.ZERO);
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.of(targetAccount));
        accountService.close(targetAccount.getAccountNumber());
        assertFalse(accountDao.accountExist(targetAccount.getAccountNumber()));
    }

    @Test
    public void failCloseAccountOnCloseNoneEmptyAccountException() throws BusinessException {
        Account targetAccount = createMockCloseAccount(BigDecimal.TEN);
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.of(targetAccount));
        exceptionRule.expect(CloseNoneEmptyAccountException.class);
        exceptionRule.expectMessage(String.format("Cannot close account [%d] with funds greater than zero amount", targetAccount.getAccountNumber()));
        accountService.close(targetAccount.getAccountNumber());
    }

    @Test
    public void failCloseAccountOnAccountNumberNotFoundException() throws BusinessException{
        Account targetAccount = createMockCloseAccount(BigDecimal.TEN);
        when(accountService.findAccountByAccNo(anyLong())).thenReturn(Optional.empty());
        exceptionRule.expect(AccountNumberNotFoundException.class);
        exceptionRule.expectMessage(String.format("Account number %d cannot be found.", targetAccount.getAccountNumber()));
        accountService.close(targetAccount.getAccountNumber());
    }
    private Account createMockCloseAccount(BigDecimal amount){
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withNationalIdNumber(9008047193475L)
                .build();
        return Account.getBuilder()
                .withBalance(amount)
                .withCustomer(customer)
                .withLastModified()
                .build();
    }

    private Account createMockTargetAccountTransfer(){
        Customer customer = Customer.getBuilder()
                .withFirstName("Gabarinocheka")
                .withLastName("ChabvondokaBanditi")
                .withNationalIdNumber(1990061628530L)
                .build();
        return Account.getBuilder()
                .withBalance(BigDecimal.valueOf(400))
                .withCustomer(customer)
                .withLastModified()
                .withCurrency("EUR")
                .build();
    }
    private Account createMockSourceAccountTransfer(){
        Customer customer = Customer.getBuilder()
                .withFirstName("Karigamombe")
                .withLastName("Tambazvinonetsa")
                .withNationalIdNumber(2019061629869L)
                .build();
        return Account.getBuilder()
                .withBalance(BigDecimal.valueOf(1200))
                .withCustomer(customer)
                .withLastModified()
                .withCurrency("EUR")
                .build();
    }

    private Account createMockAccountWithdraw(){
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withNationalIdNumber(9008047193475L)
                .build();
        return Account.getBuilder()
                .withBalance(BigDecimal.valueOf(1200))
                .withCustomer(customer)
                .withLastModified()
                .withCurrency("USD")
                .build();
    }

    private Account createMockAccount(){
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withNationalIdNumber(9008047193475L)
                .build();
        return Account.getBuilder()
                .withBalance(BigDecimal.TEN)
                .withCustomer(customer)
                .withLastModified()
                .build();
    }

    private List<Account> createMockAccounts() {
        List<Account> mockAccounts = new ArrayList<>();
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withNationalIdNumber(9008047193475L)
                .build();
        Account account1 = Account.getBuilder()
                .withBalance(BigDecimal.TEN)
                .withCustomer(customer)
                .withLastModified()
                .build();
        mockAccounts.add(account1);
        Account account2 = Account.getBuilder()
                .withBalance(BigDecimal.valueOf(100))
                .withCustomer(customer)
                .withLastModified()
                .build();
        mockAccounts.add(account2);
        return mockAccounts;
    }
}
