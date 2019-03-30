package org.chibamuio.moneytransfer.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Customer implements Serializable {

    private long nationalIdNumber;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

    public long getNationalIdNumber() {
        return nationalIdNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    private void setNationalIdNumber(long nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public static Builder getBuilder(){
        return new Builder();
    }

    public static class Builder{
        private long nationalIdNumber;
        private String firstName;
        private String lastName;
        private LocalDateTime createdAt;
        private LocalDateTime lastModified;
        private Customer customer;

        public Builder() {
        }

        public Builder withNationalIdNumber(long nationalIdNumber) {
            this.nationalIdNumber = nationalIdNumber;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withCreatedAt() {
            this.createdAt = LocalDateTime.now();
            return this;
        }

        public Builder setLastModified() {
            this.lastModified = LocalDateTime.now();
            return this;
        }

        public Customer build(){
            customer = new Customer();
            customer.setNationalIdNumber(nationalIdNumber);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setCreatedAt(createdAt);
            customer.setLastModified(lastModified);
            return customer;
        }

        public Customer getCustomer() {
            return customer;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;

        Customer customer = (Customer) o;

        if (getNationalIdNumber() != customer.getNationalIdNumber()) return false;
        if (!getFirstName().equals(customer.getFirstName())) return false;
        if (!getLastName().equals(customer.getLastName())) return false;
        if (!getCreatedAt().equals(customer.getCreatedAt())) return false;
        return getLastModified().equals(customer.getLastModified());
    }

    @Override
    public int hashCode() {
        int result = (int) (getNationalIdNumber() ^ (getNationalIdNumber() >>> 32));
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        result = 31 * result + getLastModified().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "nationalIdNumber='" + nationalIdNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", createdAt=" + createdAt +
                ", lastModified=" + lastModified +
                '}';
    }
}
