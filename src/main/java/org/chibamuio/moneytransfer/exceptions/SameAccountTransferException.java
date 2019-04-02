package org.chibamuio.moneytransfer.exceptions;

public class SameAccountTransferException extends BusinessException {

    private long originAccount;
    private long destinationAccount;

    public SameAccountTransferException(long originAccount, long destinationAccount) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
    }

    @Override
    public String getMessage() {
        return String.format("Source account (%s) and destination account (%s) are the same", originAccount, destinationAccount);
    }
}
