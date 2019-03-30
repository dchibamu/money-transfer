package org.chibamuio.moneytransfer.rest.dto;

import org.chibamuio.moneytransfer.domain.Customer;
import org.javamoney.moneta.Money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class AccountInfoDto implements Serializable {

    private final long accountNumber;
    private final BigDecimal balance;
    private final String currency;
    private final long nationalIdNumber;
    private final String firstName;
    private final String lastName;
    private final String createdAt;
    private final String lastModified;

    public AccountInfoDto(long accountNumber, BigDecimal balance, String currency, long nationalIdNumber, String firstName, String lastName, String createdAt, String lastModified) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
        this.nationalIdNumber = nationalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.lastModified = lastModified;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModified() {
        return lastModified;
    }
}
