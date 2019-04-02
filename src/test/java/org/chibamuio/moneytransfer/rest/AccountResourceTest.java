package org.chibamuio.moneytransfer.rest;

import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.exceptions.BusinessException;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;
import org.chibamuio.moneytransfer.rest.dto.AccountInfoDTO;
import org.chibamuio.moneytransfer.rest.dto.CustomerDTO;
import org.chibamuio.moneytransfer.rest.dto.DepositWithdrawalReqDTO;
import org.chibamuio.moneytransfer.rest.dto.TransferDTO;
import org.chibamuio.moneytransfer.services.AccountService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
public class AccountResourceTest extends JerseyTest {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResourceTest.class);

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private CustomerDao customerDaoMock;

    @Override
    public Application configure() {
        MockitoAnnotations.initMocks(this);
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        ResourceConfig config = new ResourceConfig(AccountResource.class);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountServiceMock).to(AccountService.class);
                bind(customerDaoMock).to(CustomerDao.class);
            }
        });
        return config;
    }

    @Test
    public void testSuccessfulOpenAccount() throws BusinessException {
        CustomerDTO customerDto = CustomerDTO.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(accountServiceMock.create(any(CustomerDTO.class))).thenReturn(createMockAccount());
        Entity<CustomerDTO> customerDtoEntity = Entity.entity(customerDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer")
                .request(MediaType.APPLICATION_JSON)
                .post(customerDtoEntity);
        assertEquals(Response.Status.CREATED.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testFailedOpenAccount() throws BusinessException {
        CustomerDTO customerDto = CustomerDTO.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(accountServiceMock.create(any(CustomerDTO.class))).thenReturn(Optional.empty());
        Entity<CustomerDTO> customerDtoEntity = Entity.entity(customerDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer")
                .request(MediaType.APPLICATION_JSON)
                .post(customerDtoEntity);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testGetAllAccounts() {
        Response response = target("/money-transfer/9008016295194")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals("Should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetAllCustomerAccounts() throws CustomerNotFoundException {
        when(accountServiceMock.getAll(anyLong())).thenReturn(createMockAccounts());
        Response response = target("/money-transfer/9008016295194")
                .request(MediaType.APPLICATION_JSON)
                .get();
        final List<AccountInfoDTO> result = response.readEntity(new GenericType<List<AccountInfoDTO>>(){});
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
        assertThat(result, hasSize(greaterThan(0)));
    }

    @Test
    public void testDeposit() {
        DepositWithdrawalReqDTO depositReqDto = new DepositWithdrawalReqDTO(1554060992324L, BigDecimal.valueOf(1200));
        Entity<DepositWithdrawalReqDTO> depositReqDtoEntity = Entity.entity(depositReqDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer/deposit")
                .request(MediaType.APPLICATION_JSON)
                .put(depositReqDtoEntity);
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
    }

    @Test
    public void testWithdrawal(){
        DepositWithdrawalReqDTO withdrawalReqDto = new DepositWithdrawalReqDTO(1554060992324L,  BigDecimal.valueOf(750));
        Entity<DepositWithdrawalReqDTO> withdrawalReqDtoEntity = Entity.entity(withdrawalReqDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer/withdraw")
                .request(MediaType.APPLICATION_JSON)
                .put(withdrawalReqDtoEntity);
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
    }

    @Test
    public void testMoneyTransfer(){
        TransferDTO transferDto = new TransferDTO(1554060992324L, 2664060994335L, BigDecimal.valueOf(750));
        Entity<TransferDTO> transferDtoEntity = Entity.entity(transferDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer/transfer")
                .request(MediaType.APPLICATION_JSON)
                .put(transferDtoEntity);
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
    }
    @Test
    public void testWrongTransfer(){
        TransferDTO transferDto = new TransferDTO(1554060992324L, 1554060992324L, BigDecimal.valueOf(750));
        Entity<TransferDTO> transferDtoEntity = Entity.entity(transferDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer/transfer")
                .request(MediaType.APPLICATION_JSON)
                .put(transferDtoEntity);
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.CLIENT_ERROR));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testBalance(){
        Response response = target("/money-transfer/balance/1554060992324")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testClose(){
        Response response = target("/money-transfer/1554060992324")
                .request(MediaType.APPLICATION_JSON)
                .delete();
        assertEquals(Response.Status.OK.getStatusCode(),  response.getStatus());
    }

    


    private Optional<Account> createMockAccount(){
        return Optional.of(Account.getBuilder()
                        .withBalance(BigDecimal.valueOf(200, 2))
                        .withCurrency("USD")
                        .withLastModified()
                        .build());
    }

    private List<Account> createMockAccounts() {
        List<Account> mockAccounts = new ArrayList<>();
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withNationalIdNumber(9008047193475L)
                .build();
        Account account = Account.getBuilder()
                .withBalance(BigDecimal.TEN)
                .withCustomer(customer)
                .withLastModified()
                .build();
        mockAccounts.add(account);
        return mockAccounts;
    }
}
