package org.chibamuio.moneytransfer.domain;


import org.javamoney.moneta.Money;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Account implements Serializable {
	private long accountNumber;
	private Money balance;
	private Customer customer;
	private LocalDateTime createdAt;
	private LocalDateTime lastModified;

	private Account(){}

	public long getAccountNumber() {
		return accountNumber;
	}

	public Money getBalance() {
		return balance;
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

	private void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setBalance(Money balance) {
		this.balance = balance;
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
		return new Builder();
	}

	public static class Builder {
		private long accountNumber;
		private Money balance;
		private Customer customer;
		private LocalDateTime createdAt;
		private LocalDateTime lastModified;
		private Account account;

		public Builder withAccountNumber(final long accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public Builder withCustomer(final Customer customer) {
			this.customer = customer;
			return this;
		}

		public Builder withCreatedAt() {
			this.createdAt = LocalDateTime.now();
			return this;
		}

		public Builder withLastModified() {
			this.lastModified = LocalDateTime.now();
			return this;
		}

		public Builder withBalance(final Money balance){
			this.balance = balance;
			return this;
		}

		public Account build(){
			account = new Account();
			account.setAccountNumber(accountNumber);
			account.setBalance(balance);
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