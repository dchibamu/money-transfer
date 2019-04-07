package org.chibamuio.moneytransfer.exceptions;

public class CloseNoneEmptyAccountException extends BusinessException {
    private long accountNumber;

    public CloseNoneEmptyAccountException(long  accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Cannot close account [%d] with funds greater than zero amount. Customer required to withdraw all funds before closing account", accountNumber);
    }
}
