package org.chibamuio.moneytransfer.rest.dto;

import java.math.BigDecimal;


public final class AccountInfoDTO implements BaseDTO<AccountInfoDTO> {

    private long accountNumber;
    private BigDecimal balance;
    private String currency;
    private long nationalIdNumber;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String lastModified;

    private AccountInfoDTO() {
    }

    public AccountInfoDTO(long accountNumber, BigDecimal balance, String currency, long nationalIdNumber, String firstName, String lastName, String createdAt, String lastModified) {
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
