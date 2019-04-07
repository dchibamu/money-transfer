package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

import static org.chibamuio.moneytransfer.util.AppConstants.ACCOUNT_NUMBER_VALIDATION_MSG;
import static org.chibamuio.moneytransfer.util.AppConstants.AMOUNT_VALIDATION_MSG;

public final class TransferDTO implements BaseDTO<TransferDTO>{
    @Positive(message = ACCOUNT_NUMBER_VALIDATION_MSG)
    private long sourceAccountNumber;
    @Positive(message = ACCOUNT_NUMBER_VALIDATION_MSG)
    private long targetAccountNumber;
    @Positive(message = AMOUNT_VALIDATION_MSG)
    private BigDecimal amount;

    public TransferDTO() {
    }

    public TransferDTO(@Positive(message = ACCOUNT_NUMBER_VALIDATION_MSG) final long sourceAccountNumber, @Positive(message = ACCOUNT_NUMBER_VALIDATION_MSG) final long targetAccountNumber, @Positive(message = AMOUNT_VALIDATION_MSG) final BigDecimal amount) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.amount = amount;
    }

    public long getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public long getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }


    @Override
    public String toString() {
        return "TransferDTO{" +
                "sourceAccountNumber=" + sourceAccountNumber +
                ", targetAccountNumber=" + targetAccountNumber +
                ", amount=" + amount +
                '}';
    }
}
