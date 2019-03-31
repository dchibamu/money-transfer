package org.chibamuio.moneytransfer.rest;

import org.chibamuio.moneytransfer.dao.CustomerDao;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.domain.Customer;
import org.chibamuio.moneytransfer.exceptions.BusinessException;
import org.chibamuio.moneytransfer.exceptions.CustomerNotFoundException;
import org.chibamuio.moneytransfer.rest.dto.AccountInfoDto;
import org.chibamuio.moneytransfer.rest.dto.CustomerDto;
import org.chibamuio.moneytransfer.rest.dto.DepositReqDto;
import org.chibamuio.moneytransfer.services.AccountService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.javamoney.moneta.Money;
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

import static org.hamcrest.Matchers.*;
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
        CustomerDto customerDto = CustomerDto.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(accountServiceMock.create(any(CustomerDto.class))).thenReturn(createMockAccount());
        Entity<CustomerDto> customerDtoEntity = Entity.entity(customerDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer")
                .request(MediaType.APPLICATION_JSON)
                .post(customerDtoEntity);
        assertEquals(Response.Status.CREATED.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testFailedOpenAccount() throws BusinessException {
        CustomerDto customerDto = CustomerDto.newCustomerDto(9008016295194L, "DOMINIC", "CHIBAMU", "USD", BigDecimal.valueOf(200000));
        when(accountServiceMock.create(any(CustomerDto.class))).thenReturn(Optional.empty());
        Entity<CustomerDto> customerDtoEntity = Entity.entity(customerDto, MediaType.APPLICATION_JSON);
        Response response = target("/money-transfer")
                .request(MediaType.APPLICATION_JSON)
                .post(customerDtoEntity);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),  response.getStatus());
    }

    @Test
    public void testUrlPathGetAllAccounts() {
        Response response = target("/money-transfer/9008016295194")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertEquals("should return status 200", Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetAllCustomerAccounts() throws CustomerNotFoundException {
        when(accountServiceMock.getAll(anyLong())).thenReturn(createMockAccounts());
        Response response = target("/money-transfer/9008016295194")
                .request(MediaType.APPLICATION_JSON)
                .get();
        final List<AccountInfoDto> result = response.readEntity(new GenericType<List<AccountInfoDto>>(){});
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
        assertThat(result, hasSize(greaterThan(0)));
    }

    @Test
    public void testSuccessfulDeposit() {
        DepositReqDto depositReqDto = new DepositReqDto(1554060992324L, "USD", BigDecimal.valueOf(1200));
        Entity<DepositReqDto> depositReqDtoEntity = Entity.entity(depositReqDto, MediaType.APPLICATION_JSON);
        Response response = target("/deposit")
                .request(MediaType.APPLICATION_JSON)
                .put(depositReqDtoEntity);
        assertThat(response.getStatusInfo().getFamily(), is(Response.Status.Family.SUCCESSFUL));
    }



    private Optional<Account> createMockAccount(){
        Money amt = Money.of(200, "USD");
        return Optional.of(Account.getBuilder()
                        .withAccountNumber()
                        .withBalance(amt)
                        .withCreatedAt()
                        .withLastModified()
                        .build());
    }

    private List<Account> createMockAccounts() {
        List<Account> mockAccounts = new ArrayList<>();
        Money amount = Money.of(BigDecimal.TEN, "ZAR");
        Customer customer = Customer.getBuilder()
                .withFirstName("Ngoni")
                .withLastName("Chibamu")
                .withCreatedAt()
                .withNationalIdNumber(9008047193475L)
                .build();
        Account account = Account.getBuilder()
                .withAccountNumber()
                .withBalance(amount)
                .withBalance(amount)
                .withCustomer(customer)
                .withCreatedAt()
                .withLastModified()
                .build();
        mockAccounts.add(account);
        return mockAccounts;
    }
}
