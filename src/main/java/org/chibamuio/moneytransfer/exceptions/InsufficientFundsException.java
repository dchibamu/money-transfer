package org.chibamuio.moneytransfer.exceptions;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InsufficientFundsException extends BusinessException {

    private BigDecimal requestedAmount;
    private String currency;
    private long accountNumber;

    public InsufficientFundsException(BigDecimal requestedAmount, String currency, long accountNumber) {
        this.requestedAmount = requestedAmount.setScale(2, RoundingMode.CEILING);
        this.currency = currency;
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Cannot withdraw %s%s. Insufficient funds in account %d", currency, requestedAmount.setScale(2, RoundingMode.CEILING), accountNumber);
    }
}
