package org.chibamuio.moneytransfer.exceptions;

public class AccountNumberNotFoundException extends Exception {

    private long accountNumber;

    public AccountNumberNotFoundException(long accountNumber) {
       this.accountNumber = accountNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Account number %d cannot be found.", accountNumber);
    }
}
