package org.chibamuio.moneytransfer.exceptions;

import org.javamoney.moneta.Money;

public class InSufficientFundsException extends BusinessException {

    private Money requestedAmount;
    private long accountNumber;

    public InSufficientFundsException(Money requestedAmount, long accountNumber) {
        this.requestedAmount = requestedAmount;
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Cannot withdraw %s%d. Insufficient funds in account %d",
                requestedAmount.getCurrency().getCurrencyCode(), requestedAmount.getNumber(), accountNumber);
    }
}
