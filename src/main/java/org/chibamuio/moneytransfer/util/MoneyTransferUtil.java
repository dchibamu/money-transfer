package org.chibamuio.moneytransfer.util;

import java.math.BigDecimal;

public class MoneyTransferUtil {
    public static BigDecimal ACCOUNT_OPENING_AMOUNT=BigDecimal.valueOf(100);

    public static long generateAccountNumber() {
        return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }
}
