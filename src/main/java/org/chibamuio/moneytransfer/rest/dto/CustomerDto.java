package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public final class CustomerDto implements BaseDto<CustomerDto> {

    @Digits(integer = 13, fraction = 0, message = "National identity number must be exactly 13 numbers")
    private long nationalIdNumber;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotBlank(message = "Currency is required")
    private String currency;
    @Min(value = 0, message = "Opening balance should not be less than zero")
    private BigDecimal amount;

    private CustomerDto() {
    }

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

    public static CustomerDto newCustomerDto(long nationalIdNumber, String firstName, String lastName, String currency, BigDecimal amount){
        return new CustomerDto(nationalIdNumber, firstName, lastName, currency, amount);
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
