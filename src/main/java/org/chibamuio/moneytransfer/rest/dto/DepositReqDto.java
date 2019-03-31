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
    @Min(value = 10)
    private BigDecimal amount;


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
