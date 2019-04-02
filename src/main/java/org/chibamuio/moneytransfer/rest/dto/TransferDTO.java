package org.chibamuio.moneytransfer.rest.dto;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

import static org.chibamuio.moneytransfer.util.AppConstants.ACCOUNT_NUMBER_VALIDATION_MSG;
import static org.chibamuio.moneytransfer.util.AppConstants.AMOUNT_VALIDATION_MSG;

public final class TransferDTO implements BaseDTO<TransferDTO>{
    @Min(value = 0L, message = ACCOUNT_NUMBER_VALIDATION_MSG)
    private long sourceAccountNumber;
    @Min(value = 0L, message = ACCOUNT_NUMBER_VALIDATION_MSG)
    private long targetAccountNumber;
    @Min(value = 0L, message = AMOUNT_VALIDATION_MSG)
    private BigDecimal amount;

    public TransferDTO() {
    }

    public TransferDTO(@Min(value = 0L, message = ACCOUNT_NUMBER_VALIDATION_MSG) final long sourceAccountNumber, @Min(value = 0L, message = ACCOUNT_NUMBER_VALIDATION_MSG) final long targetAccountNumber, @Min(value = 0L, message = AMOUNT_VALIDATION_MSG) final BigDecimal amount) {
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

}
