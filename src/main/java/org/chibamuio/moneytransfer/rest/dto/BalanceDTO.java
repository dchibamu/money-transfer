package org.chibamuio.moneytransfer.rest.dto;

import java.math.BigDecimal;

public final class BalanceDTO implements BaseDTO<BalanceDTO>{

    private final BigDecimal amount;
    private final String currency;

    public BalanceDTO(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    public String getCurrency() {
        return currency;
    }

}
