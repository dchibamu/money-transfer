package org.chibamuio.moneytransfer.rest.dto;

import java.math.BigDecimal;

public final class TransferDto {

    private long sourceAccountNumber;
    private long targetAccountNumber;
    private BigDecimal amount;
    private String currency;

    public long getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public long getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
