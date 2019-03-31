package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class DepositReqDto {

    @NotNull
    private Long accountNumber;
    @NotBlank
    private String currency;
    @Min(value = 10) //minimum amount to deposit should be at list 10 units of the currency
    private BigDecimal amount;

    private DepositReqDto(){}

    public DepositReqDto(@NotNull Long accountNumber, @NotBlank String currency, @Min(value = 10) BigDecimal amount) {
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.amount = amount;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
