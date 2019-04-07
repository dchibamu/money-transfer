package org.chibamuio.moneytransfer.rest;

import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.exceptions.BusinessException;
import org.chibamuio.moneytransfer.exceptions.SameAccountTransferException;
import org.chibamuio.moneytransfer.rest.dto.*;
import org.chibamuio.moneytransfer.services.AccountService;
import org.chibamuio.moneytransfer.util.ValidationUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.OK;
import static org.chibamuio.moneytransfer.util.AppConstants.NATIONAL_ID_NUMBER_LENGTH;
import static org.chibamuio.moneytransfer.util.AppConstants.NATIONA_ID_NUMBER_ERR_MSG;

@Path("accounts")
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
    public Response open(@Valid CustomerDTO customerDto) throws BusinessException{

        ValidationUtility.validateRequest(customerDto);
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
                    .entity(account)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/customer/{nationalIdNumber}")
    public Response getAll(
            @PathParam("nationalIdNumber")
            @Digits(integer = NATIONAL_ID_NUMBER_LENGTH, fraction = 0, message = NATIONA_ID_NUMBER_ERR_MSG)
            long nationalIdNumber) throws BusinessException {
        List<Account> accounts = accountService.getAll(nationalIdNumber);
        List<AccountInfoDTO> accountDtoList = this.mapToAccountDTO(accounts);
        return Response.ok().entity(accountDtoList).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{accountNumber}")
    public Response getOne(@PathParam("accountNumber") long accountNumber) {
        AccountInfoDTO accountInfoDTO = null;
        Optional<Account> account = accountService.findAccountByAccNo(accountNumber);
        if(account.isPresent())
            accountInfoDTO = mapToAccountDTO(account.get());
        return Response.ok().entity(accountInfoDTO).type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deposit(@Valid DepositWithdrawalReqDTO depositReqDto) throws BusinessException{
        ValidationUtility.validateRequest(depositReqDto);
        accountService.deposit(depositReqDto);
        return Response.ok().build();
    }

    @PUT
    @Path("/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response withdrawal(@Valid DepositWithdrawalReqDTO withdrawalReqDto) throws BusinessException {
        ValidationUtility.validateRequest(withdrawalReqDto);
        accountService.withdraw(withdrawalReqDto);
        return Response.ok().build();
    }

    @PUT
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@Valid TransferDTO transferDto) throws BusinessException {
        ValidationUtility.validateRequest(transferDto);
        accountService.transfer(transferDto);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{accountNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response close(@PathParam("accountNumber") long accountNumber) throws BusinessException {
        accountService.close(accountNumber);
        return Response.status(OK).build();
    }

    @GET
    @Path("/balance/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response balance(@PathParam("accountNumber") long accountNumber) throws BusinessException{
        BalanceDTO balance = accountService.balance(accountNumber);
        return Response.status(OK).entity(balance).type(MediaType.APPLICATION_JSON).build();
    }

    private List<AccountInfoDTO> mapToAccountDTO(List<Account> accounts){
       List<AccountInfoDTO> accountDtoList = new ArrayList<>();
        accounts.forEach(account -> {
            accountDtoList.add(mapToAccountDTO(account));
        });
        return accountDtoList;
    }

    private AccountInfoDTO mapToAccountDTO(Account account){
        return new AccountInfoDTO(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getCustomer().getNationalIdNumber(),
                account.getCustomer().getFirstName(),
                account.getCustomer().getLastName(),
                account.getCreatedAt().toString(),
                account.getLastModified().toString());
    }
}
