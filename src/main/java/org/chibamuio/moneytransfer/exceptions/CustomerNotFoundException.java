package org.chibamuio.moneytransfer.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    private long nationalIdNumber;
    public CustomerNotFoundException(final long nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    @Override
    public String getMessage() {
        return String.format("Customer with national identity number %d cannot be found.", nationalIdNumber);
    }
}
