package org.chibamuio.moneytransfer.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends BusinessException {

    private BigDecimal requestedAmount;
    private String currency;
    private long accountNumber;

    public InsufficientFundsException(BigDecimal requestedAmount, String currency, long accountNumber) {
        this.requestedAmount = requestedAmount;
        this.currency = currency;
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Cannot withdraw %s%f. Insufficient funds in account %d",
                currency, requestedAmount, accountNumber);
    }
}
