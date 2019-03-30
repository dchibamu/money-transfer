package org.chibamuio.moneytransfer.util;

import java.math.BigDecimal;

public class MoneyTransferUtil {
    public static BigDecimal ACCOUNT_OPENING_AMOUNT=BigDecimal.valueOf(100);
    public static int NATIONAL_ID_NUMBER_LENGTH = 13;
    public static String HOST = "http://localhost/";
    public static int PORT = 9998;
    public static long generateAccountNumber() {
        return (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
    }
}
