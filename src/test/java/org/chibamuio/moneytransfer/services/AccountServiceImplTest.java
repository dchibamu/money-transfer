package org.chibamuio.moneytransfer.services;

import org.chibamuio.moneytransfer.dao.AccountDao;
import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.dao.TransactionDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.domain.Transaction;
import org.chibamuio.moneytransfer.exceptions.BusinessException;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;
import org.chibamuio.moneytransfer.rest.dto.CustomerDTO;
import org.chibamuio.moneytransfer.services.impl.AccountServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
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
        doNothing().when(accountDao).persist(isA(Account.class));
        doNothing().when(transactionDao).persist(isA(Transaction.class));
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
        doNothing().when(accountDao).persist(isA(Account.class));
        doNothing().when(transactionDao).persist(isA(Transaction.class));
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
