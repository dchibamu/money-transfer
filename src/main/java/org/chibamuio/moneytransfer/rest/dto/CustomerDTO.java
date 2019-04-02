package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

import static org.chibamuio.moneytransfer.util.AppConstants.NATIONAL_ID_NUMBER_LENGTH;

public final class CustomerDTO implements BaseDTO<CustomerDTO> {

    @Digits(integer = NATIONAL_ID_NUMBER_LENGTH, fraction = 0, message = "National identity number must be exactly 13 numbers")
    private long nationalIdNumber;
    @NotBlank(message = "First Name is required")
    private String firstName;
    @NotBlank(message = "Last Name is required")
    private String lastName;
    @NotBlank(message = "Currency is required")
    private String currency;
    @Min(value = 0, message = "Opening balance should not be less than zero")
    private BigDecimal amount;

    private CustomerDTO() {
    }

    private CustomerDTO(long nationalIdNumber, String firstName, String lastName, String currency, BigDecimal amount) {
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

    public static CustomerDTO newCustomerDto(long nationalIdNumber, String firstName, String lastName, String currency, BigDecimal amount){
        return new CustomerDTO(nationalIdNumber, firstName, lastName, currency, amount);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "nationalIdNumber='" + nationalIdNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
