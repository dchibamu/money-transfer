package org.chibamuio.moneytransfer.rest.dto;

import java.math.BigDecimal;

public final class DepositReqDto {

    private long accountNumber;
    private String currency;
    private BigDecimal amount;


    public long getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
