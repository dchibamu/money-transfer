package org.chibamuio.moneytransfer.rest;

import org.apache.commons.lang3.StringUtils;
import org.chibamuio.moneytransfer.domain.Account;
import org.chibamuio.moneytransfer.exceptions.*;
import org.chibamuio.moneytransfer.rest.dto.*;
import org.chibamuio.moneytransfer.services.AccountService;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.*;
import javax.validation.constraints.Digits;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;

import static javax.ws.rs.core.Response.Status.OK;
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
    public Response open(@Valid CustomerDto customerDto) throws BusinessException{

        validateRequest(customerDto);
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
    @Path("/{nationalIdNumber}")
    public Response getAll(
            @PathParam("nationalIdNumber")
            @Digits(integer = 13, fraction = 0, message = "National identity number must be exactly 13 numbers")
            long nationalIdNumber) throws BusinessException {

        List<Account> accounts = accountService.getAll(nationalIdNumber);
        List<AccountInfoDto> accountDtoList = this.mapToAccountDto(accounts);
        return Response.ok().entity(accountDtoList).build();
    }

    @PUT
    @Path("/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deposit(DepositReqDto depositReqDto) throws BusinessException{
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
        return Response.status(OK).entity(jsonBalance).build();
    }

    public boolean validateAccountOpenRequest(final CustomerDto customerDto){
        if(StringUtils.isBlank(customerDto.getFirstName()) ||
                StringUtils.isBlank(customerDto.getLastName()) ||
                StringUtils.isBlank(customerDto.getCurrency()) ||
                customerDto.getAmount().compareTo(BigDecimal.ZERO) < 0)
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

    public static Validator createValidator() {
        Configuration<?> config = Validation.byDefaultProvider().configure();
        LOG.debug("config {}",config);
        ValidatorFactory factory = config.buildValidatorFactory();
        LOG.debug("factory {}",factory);

        Validator validator = factory.getValidator();
        LOG.debug("validator {}",validator);

        factory.close();
        return validator;
    }

    public static void validateRequest(BaseDto<? extends  BaseDto> dto) throws InputValidationException{
        Map<String, String> errors = new HashMap<>();
        Validator validator = createValidator();
        //LOG.debug("validator {}", validator);
        Set<ConstraintViolation<BaseDto>> violations = validator.validate(dto);
        LOG.debug("violations: {}", violations);
        if(!violations.isEmpty()){
            violations.stream().forEach( violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
            throw new InputValidationException(errors);
           /* return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();*/
        }
        LOG.debug("errors: {}", errors);
    }

}
