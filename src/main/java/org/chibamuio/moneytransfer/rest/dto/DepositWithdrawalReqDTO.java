package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class DepositWithdrawalReqDTO implements BaseDTO<DepositWithdrawalReqDTO> {

    @NotNull
    private Long accountNumber;
    @Min(value = 10) //minimum amount to deposit should be at list 10 units of the currency
    private BigDecimal amount;

    private DepositWithdrawalReqDTO(){}

    public DepositWithdrawalReqDTO(@NotNull Long accountNumber, @Min(value = 10) BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }


    public BigDecimal getAmount() {
        return amount;
    }
}
