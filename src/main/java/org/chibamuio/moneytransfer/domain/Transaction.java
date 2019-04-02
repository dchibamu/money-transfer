package org.chibamuio.moneytransfer.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public final class Transaction implements Serializable {

    private long transactionId;
    private Account sourceAccount;
    private Account destinationAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;

    public long getTransactionId() {
        return transactionId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    private void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    private void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    private void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    private void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    private void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public static Builder getBuilder(){
        return new Builder()
                .withTransactionId()
                .withCreatedAt();
    }

    public static class Builder {

        private long transactionId;
        private Account sourceAccount;
        private Account destinationAccount;
        private BigDecimal amount;
        private LocalDateTime createdAt;
        private TransactionType transactionType;
        private Transaction transaction;
        private static AtomicReference<Long> currentTime = new AtomicReference<>(Instant.now().toEpochMilli());
        private Builder withTransactionId() {
            this.transactionId = currentTime.accumulateAndGet(Instant.now().toEpochMilli(), (prev, next) -> next > prev ? next : prev + 1);
            return this;
        }

        public Builder withSourceAccount(Account sourceAccount) {
            this.sourceAccount = sourceAccount;
            return this;
        }

        public Builder withDestinationAccount(Account destinationAccount) {
            this.destinationAccount = destinationAccount;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }


        public Builder withTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        private Builder withCreatedAt() {
            this.createdAt = LocalDateTime.now();
            return this;
        }

        public Transaction getTransaction(){
            return transaction;
        }

        public Transaction build(){
            transaction = new Transaction();
            transaction.setTransactionId(transactionId);
            transaction.setSourceAccount(sourceAccount);
            transaction.setDestinationAccount(destinationAccount);
            transaction.setAmount(amount);
            transaction.setTransactionType(transactionType);
            transaction.setCreatedAt(createdAt);
            return transaction;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (getTransactionId() != that.getTransactionId()) return false;
        if (!getSourceAccount().equals(that.getSourceAccount())) return false;
        if (!getDestinationAccount().equals(that.getDestinationAccount())) return false;
        if (!getAmount().equals(that.getAmount())) return false;
        if (getTransactionType() != that.getTransactionType()) return false;
        return getCreatedAt().equals(that.getCreatedAt());
    }

    @Override
    public int hashCode() {
        int result = (int) (getTransactionId() ^ (getTransactionId() >>> 32));
        result = 31 * result + getSourceAccount().hashCode();
        result = 31 * result + getDestinationAccount().hashCode();
        result = 31 * result + getAmount().hashCode();
        result = 31 * result + getTransactionType().hashCode();
        result = 31 * result + getCreatedAt().hashCode();
        return result;
    }
}
