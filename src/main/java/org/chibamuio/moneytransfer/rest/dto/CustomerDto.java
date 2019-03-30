package org.chibamuio.moneytransfer.rest.dto;

import java.math.BigDecimal;

public final class CustomerDto {

    private final long nationalIdNumber;
    private final String firstName;
    private final String lastName;
    private final String currency;
    private final BigDecimal amount;

    private CustomerDto(long nationalIdNumber, String firstName, String lastName, String currency, BigDecimal amount) {
        this.nationalIdNumber = nationalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currency = currency;
        this.amount = amount;
    }

    public long getNationalIdNumber() {
        return nationalIdNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "nationalIdNumber='" + nationalIdNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
