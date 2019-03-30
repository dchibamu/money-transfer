package org.chibamuio.moneytransfer.rest;

import org.apache.commons.lang3.StringUtils;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.exceptions.AccountNumberNotFoundException;
import org.chibamuio.moneytransfer.exceptions.CloseNoneEmptyAccountException;
import org.chibamuio.moneytransfer.exceptions.InSufficientFundsException;
import org.chibamuio.moneytransfer.rest.dto.*;
import org.chibamuio.moneytransfer.services.AccountService;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.chibamuio.moneytransfer.util.MoneyTransferUtil.ACCOUNT_OPENING_AMOUNT;
import static org.chibamuio.moneytransfer.util.MoneyTransferUtil.NATIONAL_ID_NUMBER_LENGTH;

@Path("money-transfer")
@RequestScoped
public class AccountResource {

    private AccountService accountService;
    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);
    @Inject
    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response open(final CustomerDto customerDto){

        if(!validateAccountOpenRequest(customerDto)){
            return Response.status(BAD_REQUEST).build();
        }
        Optional<Account> account = accountService.create(customerDto);
        if(account.isPresent()) {
            return Response
                    .status(Response.Status.CREATED)
                    .entity(account)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }else {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Unable to create account")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{nationalIdNumber}")
    public Response getAll(@PathParam("nationalIdNumber") long nationalIdNumber) {
        List<Account> accounts = accountService.getAll(nationalIdNumber);
        List<AccountInfoDto> accountDtoList = this.mapToAccountDto(accounts);
        return Response.ok().entity(accountDtoList).build();
    }

    @PUT
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deposit(DepositReqDto depositReqDto) throws AccountNumberNotFoundException{
        accountService.deposit(depositReqDto);
        return Response.ok().build();
    }

    @PUT
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdrawal(WithdrawalReqDto withdrawalReqDto) throws AccountNumberNotFoundException, InSufficientFundsException{
        accountService.withdraw(withdrawalReqDto);
        return Response.ok().build();
    }

    @PUT
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(TransferDto transferDto) throws AccountNumberNotFoundException, InSufficientFundsException{
        //perform validation of input
        accountService.transfer(transferDto);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{accountNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response close(@PathParam("accountNumber") long accountNumber) throws AccountNumberNotFoundException, CloseNoneEmptyAccountException {
        accountService.close(accountNumber);
        return Response.status(OK).build();
    }

    @GET
    @Path("/balance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response balance(@PathParam("accountNumber") long accountNumber) throws AccountNumberNotFoundException{
        Money balance = accountService.balance(accountNumber);
        Map<String, String> jsonBalance = new HashMap<>();
        jsonBalance.put("currency", balance.getCurrency().getCurrencyCode());
        jsonBalance.put("amount", balance.getNumberStripped().toString());
        //String jsonBalance = "{ \"currency\" : "+balance.getCurrency()+", \"balance\" : "+balance.getNumberStripped()+"}";
        return Response.status(OK).entity(jsonBalance).build();
    }

    public boolean validateAccountOpenRequest(final CustomerDto customerDto){
        if( !validateNationalIdNumber(customerDto.getNationalIdNumber()) || StringUtils.isBlank(customerDto.getFirstName()) ||
                StringUtils.isBlank(customerDto.getLastName()) ||
                StringUtils.isBlank(customerDto.getCurrency()) ||
                customerDto.getAmount().compareTo(ACCOUNT_OPENING_AMOUNT) < 0)
            return false;
        return true;
    }

    public boolean validateNationalIdNumber(final long nationalIdNumber){
        int length = 0;
        long temp = 1;
        while(temp <= nationalIdNumber){
            length++;
            temp *= 10;
        }
        return length == NATIONAL_ID_NUMBER_LENGTH;
    }

    private List<AccountInfoDto> mapToAccountDto(List<Account> accounts){
       List<AccountInfoDto> accountDtoList = new ArrayList<>();
        accounts.forEach(account -> {
            AccountInfoDto accountDto = new AccountInfoDto(
                    account.getAccountNumber(),
                    account.getBalance().getNumberStripped(),
                    account.getBalance().getCurrency().getCurrencyCode(),
                    account.getCustomer().getNationalIdNumber(),
                    account.getCustomer().getFirstName(),
                    account.getCustomer().getLastName(),
                    account.getCreatedAt().toString(),
                    account.getLastModified().toString());
            accountDtoList.add(accountDto);
        });
        return accountDtoList;
    }
}
