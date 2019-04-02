package org.chibamuio.moneytransfer.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Account implements Serializable {
	private long accountNumber;
	private BigDecimal balance;
	private String currency;
	private Customer customer;
	private LocalDateTime createdAt;
	private LocalDateTime lastModified;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

	private Account(){
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

	public Customer getCustomer() {
		return customer;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getLastModified() {
		return lastModified;
	}

	public Lock getReadLock() {
	    return readLock;
    }

    public Lock getWriteLock(){
	    return writeLock;
    }

	private void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	private void setCurrency(String currency) {
		this.currency = currency;
	}

	private void setCustomer(Customer customer) {
		this.customer = customer;
	}

	private void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public void setLastModified(LocalDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public static Builder getBuilder(){
		return new Builder().withAccountNumber().withCreatedAt();
	}

	public static class Builder {
		private long accountNumber;
		private BigDecimal balance;
		private String currency;
		private Customer customer;
		private LocalDateTime createdAt;
		private LocalDateTime lastModified;
		private Account account;
		private static AtomicReference<Long> currentTime = new AtomicReference<>(Instant.now().toEpochMilli());
		private Builder withAccountNumber() {
			this.accountNumber = currentTime.accumulateAndGet(Instant.now().toEpochMilli(), (prev, next) -> next > prev ? next : prev + 1);
			return this;
		}

		public Builder withCustomer(final Customer customer) {
			this.customer = customer;
			return this;
		}

		private Builder withCreatedAt() {
			this.createdAt = LocalDateTime.now();
			return this;
		}

		public Builder withLastModified() {
			this.lastModified = LocalDateTime.now();
			return this;
		}

		public Builder withBalance(final BigDecimal balance){
			this.balance = balance;
			return this;
		}

		public Builder withCurrency(final String currency){
			this.currency = currency;
			return this;
		}

		public Account build(){
			account = new Account();
			account.setAccountNumber(accountNumber);
			account.setBalance(balance);
			account.setCurrency(currency);
			account.setCustomer(customer);
			account.setCreatedAt(LocalDateTime.now());
			account.setLastModified(LocalDateTime.now());
			return account;
		}

		public Account getAccount(){
			return account;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Account)) return false;

		Account account = (Account) o;

		if (getAccountNumber() != account.getAccountNumber()) return false;
		if (!getBalance().equals(account.getBalance())) return false;
		if (!getCustomer().equals(account.getCustomer())) return false;
		if (!getCreatedAt().equals(account.getCreatedAt())) return false;
		return getLastModified().equals(account.getLastModified());
	}

	@Override
	public int hashCode() {
		int result = (int) (getAccountNumber() ^ (getAccountNumber() >>> 32));
		result = 31 * result + getBalance().hashCode();
		result = 31 * result + getCustomer().hashCode();
		result = 31 * result + getCreatedAt().hashCode();
		result = 31 * result + getLastModified().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountNumber=" + accountNumber +
				", balance=" + balance +
				", customer=" + customer +
				", createdAt=" + createdAt +
				", lastModified=" + lastModified +
				'}';
	}
}